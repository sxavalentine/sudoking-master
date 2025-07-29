package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.ChangeLog;
import com.gianfro.games.entities.Link;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.entities.SudokuCell;

import java.util.*;

public class RemotePairs {

    /**
     * If in a Sudoku TODO: complete
     */

    public static final String REMOTE_PAIRS = "REMOTE_PAIRS";

    public static Set<ChangeLog> check(Sudoku sudoku) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>(findRemotePairs(sudoku));
        return changeLogs;
    }

    private static List<ChangeLog> findRemotePairs(Sudoku sudoku) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        Map<List<Integer>, List<SudokuCell>> biValueCellsMap = new HashMap<>();
        for (SudokuCell cell : sudoku.getCells()) {
            if (cell.getCandidates().size() == 2) {
                biValueCellsMap.computeIfAbsent(cell.getCandidates(), k -> new ArrayList<>()).add(cell);
            }
        }
        biValueCellsMap.entrySet().forEach(entry -> {
            List<SudokuCell> sameTwoCandidatesCells = entry.getValue();
            if (sameTwoCandidatesCells.size() >= 4) {
                List<List<Link>> allChains = new ArrayList<>();

                for (SudokuCell cell : sameTwoCandidatesCells) {
                    List<Link> chain = new ArrayList<>();
                }

                Map<SudokuCell, Set<SudokuCell>> graph = new HashMap<>();
                for (SudokuCell cell : sameTwoCandidatesCells) {
                    graph.put(cell, new HashSet<>());
                }
                for (SudokuCell cell : sameTwoCandidatesCells) {
                    for (SudokuCell otherCell : sameTwoCandidatesCells) {
                        if (cell.canSeeOther(otherCell)) {
                            graph.get(cell).add(otherCell);
                            graph.get(otherCell).add(cell);
                        }
                    }
                }

                for (SudokuCell startNode : graph.keySet()) {
                    List<SudokuCell> currentPath = new ArrayList<>();
                    Set<SudokuCell> visitedNodes = new HashSet<>();

                    currentPath.add(startNode);
                    visitedNodes.add(startNode);

                }
            }
        });
        return changeLogs;
    }

    /**
     * Ricerca ricorsiva DFS per trovare catene Remote Pairs.
     *
     * @param sudoku                   L'oggetto Sudoku corrente.
     * @param startNode                Il nodo di partenza della catena (fisso per questa DFS).
     * @param currentNode              Il nodo corrente che stiamo esaminando.
     * @param currentPath              La lista delle celle nel percorso attuale.
     * @param visitedNodes             Il set dei nodi già visitati in questo percorso per evitare cicli.
     * @param graph                    La rappresentazione del grafo.
     * @param allValidRemotePairChains Lista per raccogliere le catene Remote Pairs trovate.
     * @param candidatesPair           La coppia di candidati che definisce la Remote Pair (es. {1,5}).
     */
    private static void findRemotePairChain(
            Sudoku sudoku,
            SudokuCell startNode,
            SudokuCell currentNode,
            List<SudokuCell> currentPath,
            Set<SudokuCell> visitedNodes,
            Map<SudokuCell, Set<SudokuCell>> graph,
            List<List<SudokuCell>> allValidRemotePairChains,
            List<Integer> candidatesPair
    ) {
        // Condizione di successo per una Remote Pair:
        // 1. La catena deve avere un numero dispari di nodi (che significa un numero pari di link).
        //    Esempio: A-B-C (3 nodi, 2 link) -> A e C sono "allineati" sullo stesso candidato.
        // 2. Il nodo di partenza e il nodo corrente NON devono vedersi (non devono essere nella stessa riga/colonna/blocco).
        // 3. La catena deve avere almeno 3 nodi per essere significativa.
        if (currentPath.size() >= 3 && currentPath.size() % 2 == 1) { // Dispari di nodi = pari di link
            if (!startNode.canSeeOther(currentNode)) {
                // Trovata una catena Remote Pair valida.
                // Aggiungila solo se non è già stata trovata (potrebbe essere raggiunta da percorsi diversi).
                // Per evitare duplicati di catene invertite (A-B-C vs C-B-A), potresti normalizzare la catena (es. ordinarla).
                List<SudokuCell> foundChain = new ArrayList<>(currentPath);
                // Non è necessario controllare se la catena è già stata aggiunta se startNode è iterato esternamente
                // e la catena è sempre registrata dal suo startNode e dal suo percorso specifico.
                // Tuttavia, per essere sicuri che non si aggiungano catene duplicate (es. A->B->C e C->B->A)
                // si potrebbe normalizzare la catena prima di aggiungerla.
                // Es. Collections.sort(foundChain, Comparator.comparing(SudokuCell::getIndex)); // se SudokuCell ha getIndex()
                // allValidRemotePairChains.add(foundChain);
                // Meglio aggiungere la catena come è stata trovata e poi filtrare i duplicati alla fine
                // o utilizzare un Set<List<SudokuCell>> con un Custom Comparator/hashCode
                allValidRemotePairChains.add(foundChain);
            }
        }

        // Condizione di terminazione per evitare catene eccessivamente lunghe
//        if (currentPath.size() >= MAX_CHAIN_LENGTH) {
//            return;
//        }

        // Esplora i vicini (celle connesse nel grafo)
        Set<SudokuCell> neighbors = graph.get(currentNode);
        if (neighbors != null) {
            for (SudokuCell nextNode : neighbors) {
                if (!visitedNodes.contains(nextNode)) { // Evita cicli all'interno dello stesso percorso
                    currentPath.add(nextNode);
                    visitedNodes.add(nextNode); // Aggiungi ai visitati prima della ricorsione
                    findRemotePairChain(sudoku, startNode, nextNode, currentPath, visitedNodes,
                            graph, allValidRemotePairChains, candidatesPair);
                    // Backtrack: rimuovi dall'attuale percorso e dai visitati
                    visitedNodes.remove(nextNode);
                    currentPath.remove(currentPath.size() - 1);
                }
            }
        }
    }


}
