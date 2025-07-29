package com.gianfro.games.techniques.advanced;

import com.gianfro.games.entities.*;
import com.gianfro.games.utils.SudokuList;
import com.gianfro.games.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class ChainNew {

    public static List<List<Link>> findAllChains(Sudoku sudoku) {
        List<SudokuCell> emptyCells = sudoku.getCells().stream().filter(SudokuCell::isEmpty).toList();
        Map<Integer, List<StrongLink>> strongLinksMap = ChainUtils.getStrongLinksMap(sudoku);
        List<List<Link>> chains = new ArrayList<>();
        for (Integer candidate : strongLinksMap.keySet()) {
            List<StrongLink> strongLinks = strongLinksMap.get(candidate);
            for (StrongLink sl : strongLinks) {

                SudokuCell cellA = sl.getLinkA();
                Link aOffLink = new Link(cellA, 0, candidate);
                Link aOnLink = new Link(cellA, candidate, 0);
                if (cellA.getCandidates().size() == 2) {
                    int otherCandidate = cellA.getCandidates().get(0) != candidate ?
                            cellA.getCandidates().get(0) : cellA.getCandidates().get(1);
                    aOffLink.setNumberOn(otherCandidate);
                    aOnLink.setNumberOff(otherCandidate);
                }
                SudokuCell cellB = sl.getLinkB();
                Link bOffLink = new Link(cellB, 0, candidate);
                Link bOnLink = new Link(cellB, candidate, 0);
                if (cellB.getCandidates().size() == 2) {
                    int otherCandidate = cellB.getCandidates().get(0) != candidate ?
                            cellB.getCandidates().get(0) : cellB.getCandidates().get(1);
                    bOffLink.setNumberOn(otherCandidate);
                    bOnLink.setNumberOff(otherCandidate);
                }

                List<Link> aOffChain = List.of(aOffLink);
                List<Link> bOffChain = List.of(bOffLink);
                List<Link> aOnChain = List.of(aOnLink);
                List<Link> bOnChain = List.of(bOnLink);
                List<List<Link>> additions = Arrays.asList(aOffChain, bOffChain, aOnChain, bOnChain);

                while (!additions.isEmpty()) {
                    List<List<Link>> newAdditions = new ArrayList<>();
                    for (List<Link> linkList : additions) {
                        //TODO REMOVE DEBUG ONLY:
                        if (linkList.toString().equals("[F5(+4-8), H5(+1-4), H4(+9-1), C4(+8-9)]")) {
                            System.out.println("TODO REMOVE");
                        }
                        Link lastLink = linkList.get(linkList.size() - 1);
                        int previousLinkIndex = linkList.size() < 2 ? -1 : linkList.get(linkList.size() - 2).getIndex();

                        Set<Link> implications = getImplications(lastLink, emptyCells, strongLinksMap);
                        for (Link newLink : implications) {

                            Link alreadyInChain = linkList.stream().filter(l -> l.getIndex() == newLink.getIndex()).findFirst().orElse(null);

                            ArrayList<Link> newChain = new ArrayList<>(linkList);
                            newChain.add(newLink);

                            if (alreadyInChain != null) {
                                if (previousLinkIndex != newLink.getIndex()) {
                                    chains.add(newChain);//TODO: anche se non ci sono contraddizioni (continuous nice loop)
                                }
                            } else {
                                if (newChain.size() > 2) {
                                    chains.add(newChain);//TODO: forse è il caso di aggiungere solo se non ci sono implicazioni ?
                                }
                                newAdditions.add(newChain);
                            }
                        }
                    }
                    additions = newAdditions;
                }
            }
//            chains.forEach(chain -> System.out.println(chain));//TODO REMOVE
        }
        return chains;
    }

    private static Set<Link> getImplications(Link link, List<SudokuCell> emptySudokuCells, Map<Integer, List<StrongLink>> strongLinksMap) {

        List<SudokuCell> peers = link.getEmptyPeers(emptySudokuCells);

        Set<Link> implications = new HashSet<>();
        int numOn = link.getNumberOn();
        int numOff = link.getNumberOff();

        if (numOn > 0) {
            List<SudokuCell> linkedOnNumberOn = new ArrayList<>();
            for (SudokuCell peer : peers) {
                if (peer.getCandidates().contains(numOn)) {
                    linkedOnNumberOn.add(peer);
                }
            }
            for (SudokuCell cell : linkedOnNumberOn) {
                Link weakLink = new Link(cell, 0, numOn);
                if (weakLink.getCell().getCandidates().size() == 2) {
                    weakLink.setNumberOn(weakLink.getCell().getCandidates().get(0) != numOn ?
                            weakLink.getCell().getCandidates().get(0) : weakLink.getCell().getCandidates().get(1));
                }
                implications.add(weakLink);
            }
        }
        if (numOff > 0) {
            for (House house : House.values()) {
                int houseNumber = link.getHouseNumber(house);
                List<SudokuCell> linkedOnNumberOff = peers.stream().filter(
                                p -> p.getCandidates().contains(numOff) && p.getHouseNumber(house) == houseNumber)
                        .toList();
                //if there is only a cell that can contain that candidate
                if (linkedOnNumberOff.size() == 1) {
                    SudokuCell linkCell = linkedOnNumberOff.get(0);
                    boolean weakLinkEdited = false;
                    //check if the StronglyLinked cell was also WeaklyLinked before
                    for (Link weakLink : implications) {
                        if (weakLink.getCell() == linkCell) {
                            weakLink.setNumberOn(numOff);
                            weakLinkEdited = true;
                            break;
                        }
                    }
                    //if no Link was created before
                    if (!weakLinkEdited) {
                        boolean hasStrongLinks = false;
                        for (int candidate : linkCell.getCandidates()) {
                            if (candidate != numOff) {
                                //for each candidate for which it has a strongLink
                                if (strongLinksMap.get(candidate) != null &&
                                        !strongLinksMap.get(candidate).stream().filter(
                                                        sl -> sl.getLinkA() == linkCell || sl.getLinkB() == linkCell).toList()
                                                .isEmpty()) {
                                    implications.add(new Link(linkCell, numOff, candidate));
                                    hasStrongLinks = true;
                                }
                            }
                        }
                        // if no strongLinks were found
                        if (!hasStrongLinks) {
                            implications.add(new Link(linkCell, numOff, 0));
                        }
                    }
                }
            }
        }
        return implications;
    }

    public static Map<List<Link>, List<Link>> findUniqueChains(Sudoku sudoku) {
        List<List<Link>> chains = findAllChains(sudoku);

        //the Integers in the pair are respectively the index of the first and last link of the chain
        Map<List<Link>, List<Link>> chainsMap = new HashMap<>();
        for (List<Link> chain : chains) {
            Link firstLink = chain.get(0);
            Link lastLink = chain.get(chain.size() - 1);

            List<Link> pair;
            if (lastLink.getIndex() > firstLink.getIndex()) {
                pair = List.of(firstLink, lastLink);
            } else {
                pair = List.of(lastLink, firstLink);
            }
            List<Link> value = chainsMap.get(pair);
            if (value == null || value.size() > chain.size()) {
                chainsMap.put(pair, chain);
            }
        }
        //TODO REMOVE
        chainsMap.entrySet().forEach(e -> {
            System.out.println(e.getValue());
        });

        List<List<Link>> xChain = new ArrayList<>();//TODO REMOVE
        List<List<Link>> remotePairs = new ArrayList<>();//TODO REMOVE
        List<List<Link>> xyChain = new ArrayList<>();//TODO REMOVE
        List<List<Link>> discontinuousNiceLoop = new ArrayList<>();//TODO REMOVE
        List<List<Link>> loops = new ArrayList<>();//TODO REMOVE

        chainsMap.entrySet().forEach(entry -> {
            List<Link> chain = entry.getValue();
            Link first = chain.get(0);
            Link last = chain.get(chain.size() - 1);

            if (first.getIndex() != last.getIndex()) {
                if (first.getNumberOff() == last.getNumberOn()) {
                    List<SudokuCell> toBeSkimmed = sudoku.getCells().stream().filter(
                                    c -> c.canSeeOther(first) &&
                                            c.canSeeOther(last) &&
                                            c.getCandidates().contains(first.getNumberOff()) &&
                                            !chain.stream().map(link -> link.getCell()).toList().contains(c))
                            .toList();
                    if (!toBeSkimmed.isEmpty()) {
                        Set<Integer> digits = new HashSet<>();
                        boolean isOff = true;
                        for (Link link : chain) {
                            digits.add(isOff ? link.getNumberOff() : link.getNumberOn());
                            if (digits.size() > 1) break;
                            isOff = !isOff;
                        }
                        if (digits.size() == 1) {
                            Set<List<Integer>> pairs = chain.stream().map(link -> link.getCell().getCandidates()).collect(Collectors.toSet());
                            if (pairs.size() == 1) {
                                //REMOTE PAIRS
                                System.out.println("REMOTE PAIRS on pair " + chain.get(0).getCell().getCandidates() + ": " + chain);
                                toBeSkimmed.forEach(tbs -> System.out.println(tbs));
                                System.out.println("#####");
                                remotePairs.add(chain);
                            } else {
                                //X CHAIN
                                System.out.println("X-CHAIN on number " + first.getNumberOff() + ": " + chain);
                                toBeSkimmed.forEach(tbs -> System.out.println(tbs));
                                System.out.println("#####");
                                xChain.add(chain);
                            }
                        } else {
                            boolean allBivalue = true;
                            for (Link link : chain) {
                                if (link.getCell().getCandidates().size() > 2) {
                                    allBivalue = false;
                                    break;
                                }
                            }
                            if (allBivalue) {
                                System.out.println("XY CHAIN: " + chain);
                                toBeSkimmed.forEach(tbs -> System.out.println(tbs));
                                System.out.println("#####");
                                xyChain.add(chain);
                            }
                        }
                    }
                }
            } else {
                if (first.getNumberOff() == last.getNumberOn() || first.getNumberOn() == last.getNumberOff()) {
                    System.out.println("DISCONTINUOUS NICE LOOP: " + chain);
                    System.out.println(chain.get(0));
                    System.out.println("#####");
                    discontinuousNiceLoop.add(chain);
                }
                loops.add(chain);
            }
        });
        System.out.println("ALL CHAIN FOUND: " + chains.size());
        System.out.println("TOTAL DIFFERENT CHAINS: " + chainsMap.keySet().size());
        System.out.println("X-CHAIN FOUND: " + xChain.size());
        System.out.println("REMOTE PAIRS FOUND: " + remotePairs.size());
        System.out.println("XY-CHAIN FOUND: " + xyChain.size());
        System.out.println("DISCONTINUOUS NICE LOOP FOUND: " + discontinuousNiceLoop.size());
        System.out.println("LOOP FOUND: " + loops.size());

        return chainsMap;
    }

    public static void main(String[] args) {
        Sudoku sudoku = null;
        sudoku = Sudoku.fromString(SudokuList.TEST_X_CHAIN_1);
        sudoku = Sudoku.fromString(SudokuList.TEST_X_CHAIN_2);
        sudoku = Sudoku.fromString(SudokuList.TEST_REMOTE_PAIRS_1);
        sudoku = Sudoku.fromString("500300296263590807900762050100635072056020000020000065830106520402050630605203700");
        sudoku = Sudoku.fromString(SudokuList.DISCOUNTINUOUS_NICE_LOOP);
//        sudoku = Sudoku.fromString("513000829678219543429008167390105086050000030006000950060003018030081690001602375");
        sudoku = Sudoku.fromString(SudokuList.TEST_XY_CHAIN_4);
        findUniqueChains(sudoku);
        Utils.megaGrid(sudoku);
    }
}
