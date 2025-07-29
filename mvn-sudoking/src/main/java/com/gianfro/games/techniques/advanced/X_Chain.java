package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.entities.deductions.CellSkimmed;

import java.util.*;


public class X_Chain {

    public static final String X_CHAIN = "X-CHAIN";

    public static Set<ChangeLog> check(Sudoku sudoku, Map<Integer, List<StrongLink>> strongLinksMap) {
        Set<ChangeLog> changeLogs = new LinkedHashSet<>(xChain(sudoku, strongLinksMap));
        return changeLogs;
    }

    private static List<ChangeLog> xChain(Sudoku sudoku, Map<Integer, List<StrongLink>> strongLinksMap) {
        List<ChangeLog> changeLogs = new ArrayList<>();
        strongLinksMap.keySet().forEach(candidate -> {
            List<StrongLink> strongLinks = strongLinksMap.get(candidate);

            List<List<Link>> allChains = new ArrayList<>();
            for (StrongLink strongLink : strongLinks) {

                List<Link> chainWithOffA = List.of(
                        new Link(strongLink.getLinkA(), 0, candidate),
                        new Link(strongLink.getLinkB(), candidate, 0)
                );

                List<List<Link>> newChains = List.of(chainWithOffA);

                while (!newChains.isEmpty()) {
                    List<List<Link>> chainContinuationsFound = new ArrayList<>();
                    for (List<Link> linkList : newChains) {
                        List<SudokuCell> chainCells = linkList.stream().map(Link::getCell).toList();
                        Link lastLink = linkList.get(linkList.size() - 1);
                        Link secondLastLink = linkList.get(linkList.size() - 2);

                        List<List<Link>> chainContinuations = findNextLink(lastLink.getCell(), secondLastLink.getCell(), chainCells, strongLinks);
                        for (List<Link> continuation : chainContinuations) {
                            List<Link> newChain = new ArrayList<>(linkList);
                            newChain.addAll(continuation);
                            chainContinuationsFound.add(newChain);
                            allChains.add(newChain);
                        }
                    }
                    newChains = chainContinuationsFound;
                }
            }
            allChains.forEach(System.out::println);
            for (List<Link> chain : allChains) {
                Link first = chain.get(0);
                Link last = chain.get(chain.size() - 1);
                List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                c -> c.canSeeOther(first) &&
                                        c.canSeeOther(last) &&
                                        c.getCandidates().contains(candidate))
                        .toList();
                if (!toBeSkimmed.isEmpty()) {
                    Set<CellSkimmed> deductions = new HashSet<>();
                    List<Integer> removedCandidates = List.of(candidate);
                    for (SudokuCell cell : toBeSkimmed) {
                        deductions.add(
                                CellSkimmed.builder()
                                        .solvingTechnique(X_CHAIN)
                                        .cell(cell)
                                        .removedCandidates(removedCandidates)
                                        .build());
                    }
                    List<ChangeLogUnitMember> unitMembers = new ArrayList<>(chain);
                    ChangeLog changeLog = ChangeLog.builder()
                            .unitExamined(removedCandidates)
                            .unitMembers(unitMembers)
                            .solvingTechnique(X_CHAIN)
                            .changes(new ArrayList<>(deductions))//TODO: sort by cell.index
                            .build();
                    changeLogs.add(changeLog);
                }
            }
        });
        return changeLogs;
    }

    /**
     * TODO: A little complex. Let's say that we have a chain [A, B].
     *
     * @param toBeLinked    is B
     * @param notToBeLinked is A
     * @param chain         the current chain (in order to not add links already part of the chain and avoid loops)
     * @param strongLinks   is the list of all the strongLinks on a certain candidate
     * @return the list of list of possible continuations of the chain [[C1,D1], [C2,D2]]
     */
    private static List<List<Link>> findNextLink(SudokuCell toBeLinked, SudokuCell notToBeLinked, List<SudokuCell> chain, List<StrongLink> strongLinks) {
        List<List<Link>> chainContinuations = new ArrayList<>();
        List<StrongLink> linked = strongLinks.stream().filter(
                        sl -> (sl.getLinkA().canSeeOther(toBeLinked) ||
                                sl.getLinkB().canSeeOther(toBeLinked)) &&
                                !chain.contains(sl.getLinkA()) &&
                                !chain.contains(sl.getLinkB()) &&
                                sl.getLinkA() != notToBeLinked &&
                                sl.getLinkB() != notToBeLinked)
                .toList();
        for (StrongLink strongLink : linked) {
            if (strongLink.getLinkA().canSeeOther(toBeLinked)) {
                chainContinuations.add(List.of(
                        new Link(strongLink.getLinkA(), 0, strongLink.getSharedCandidate()),
                        new Link(strongLink.getLinkB(), strongLink.getSharedCandidate(), 0)
                ));
            } else {
                chainContinuations.add(List.of(
                        new Link(strongLink.getLinkB(), 0, strongLink.getSharedCandidate()),
                        new Link(strongLink.getLinkA(), strongLink.getSharedCandidate(), 0)
                ));
            }
        }
        return chainContinuations;
    }
}
