package com.gianfro.games.scarti;

public class ScartiSudoku {

    // PER TUTTI I METODI SEGUENTI, GLI INDICI DEI PARAMETRI PARTONO DA 1, NON DA 0
//	private List<Integer> getNumeriRiga(int indiceRiga) {
//		List<Integer> numeriRiga = new ArrayList<>();
//		for (int numero : righe.get(indiceRiga - 1)) {
//			if (numero != 0) {
//				numeriRiga.add(numero);
//			}
//		}
//		return numeriRiga;
//	}
//
//	private List<Integer> getNumeriMancantiRiga(int indiceRiga) {
//		List<Integer> numeriRiga = getNumeriRiga(indiceRiga);
//		List<Integer> numeriMancantiRiga = new ArrayList<>();
//		for (int numero : CIFRE) {
//			if (!numeriRiga.contains(numero)) {
//				numeriMancantiRiga.add(numero);
//			}
//		}
//		return numeriMancantiRiga;
//	}
//
//	private List<Integer> getNumeriColonna(int indiceColonna) {
//		List<Integer> numeriColonna = new ArrayList<>();
//		for (int numero : colonne.get(indiceColonna - 1)) {
//			if (numero != 0) {
//				numeriColonna.add(numero);
//			}
//		}
//		return numeriColonna;
//	}
//
//	private List<Integer> getNumeriQuadrato(int indiceQuadrato) {
//		List<Integer> numeriQuadrato = new ArrayList<>();
//		List<Integer> quadratoEsaminato = quadrati.get(indiceQuadrato - 1);
//		for (int numero : quadratoEsaminato) {
//			if (numero != 0) {
//				numeriQuadrato.add(numero);
//			}
//		}
//		return numeriQuadrato;
//	}
//	
//	private List<List<Integer>> getQuadratiTernaRighe(int terzettoRighe) {
//		List<List<Integer>> quadratiTerna = quadrati.subList(terzettoRighe * 3, 3 + (terzettoRighe * 3));
//		return quadratiTerna;
//	}
//
//	private List<List<Integer>> getQuadratiTernaColonne(int terzettoColonne) {
//		List<List<Integer>> quadratiTerna = new ArrayList<>();
//		for (int i : INDICI_02) {
//			quadratiTerna.add(quadrati.get(terzettoColonne + (3 * i)));
//		}
//		return quadratiTerna;
//	}
//
//	private List<Integer> getNumeriQuadrato(List<Integer> quadrato) {
//		List<Integer> numeriQuadrato = new ArrayList<>();
//		for (int numero : quadrato) {
//			if (numero != 0) {
//				numeriQuadrato.add(numero);
//			}
//		}
//		return numeriQuadrato;
//	}

//	private List<Cambiamento> controlloTerzettiRighe() {
//		List<Cambiamento> cambiamenti = new ArrayList<>();
//		for (int numero : CIFRE) {
//			for (int terzettoRighe : INDICI_02) {
//				List<Integer> righeTernaOspitali = new ArrayList<>(INDICI_02);
//				List<List<Integer>> quadratiTerna = getQuadratiTernaRighe(terzettoRighe);
//				List<Integer> quadratiOspitali = new ArrayList<>(INDICI_02);
//				for (int quadrato : INDICI_02) {
//					List<List<Integer>> righeQuadrato = getRigheQuadrato(quadratiTerna.get(quadrato));
//					for (int riga : INDICI_02) {
//						if (righeQuadrato.get(riga).contains(numero)) {
//							righeTernaOspitali.remove(new Integer(riga));
//							quadratiOspitali.remove(new Integer(quadrato));
//						}
//					}
//				}
//				if (righeTernaOspitali.size() == 1) {
//					List<Integer> quadratoOspitale = quadratiTerna.get(quadratiOspitali.get(0));
//					List<List<Integer>> righeQuadratoOspitale = getRigheQuadrato(quadratoOspitale);
//					if (Collections.frequency(righeQuadratoOspitale.get(righeTernaOspitali.get(0)), 0) == 1) {
//						int indiceCarattere = righeQuadratoOspitale.get(righeTernaOspitali.get(0)).indexOf(0);
//						cambiamenti.add(
//								new Cambiamento(
//										((3 * terzettoRighe) + righeTernaOspitali.get(0) + 1),
//										((3 * quadratiOspitali.get(0) + (indiceCarattere + 1))), 
//										numero));
//					} else {
//						List<Integer> colonneQuadratoOspitali = new ArrayList<>(INDICI_02);
//						int contoColonne = 0;
//						for (int cifra : righeQuadratoOspitale.get(righeTernaOspitali.get(0))) {
//							if (cifra != 0) {
//								colonneQuadratoOspitali.remove(new Integer(contoColonne));
//							} else {
//								if (colonne.get((3 * quadratiOspitali.get(0)) + contoColonne).contains(numero)) {
//									colonneQuadratoOspitali.remove(new Integer(contoColonne));
//								}
//							}
//							contoColonne++;
//						}
//						if (colonneQuadratoOspitali.size() == 1) {
//							cambiamenti.add(
//									new Cambiamento(
//											((3 * terzettoRighe) + righeTernaOspitali.get(0) + 1),
//											(3 * quadratiOspitali.get(0) + colonneQuadratoOspitali.get(0) + 1), 
//											numero));
//						}
//					}
//				}
//			}
//		}
//		return cambiamenti;
//	}
//
//	private List<Cambiamento> controlloTerzettiColonne() {
//		List<Cambiamento> cambiamenti = new ArrayList<>();
//		for (int numero : CIFRE) {
//			for (int terzettoColonne : INDICI_02) {
//				List<Integer> colonneTernaOspitali = new ArrayList<>(INDICI_02);
//				List<List<Integer>> quadratiTerna = getQuadratiTernaColonne(terzettoColonne);
//				List<Integer> quadratiOspitali = new ArrayList<>(INDICI_02);
//				for (int quadrato : INDICI_02) {
//					List<Integer> quadratoEsaminato = quadrati.get(terzettoColonne + (3 * quadrato));
//					List<List<Integer>> colonneQuadrato = getColonneQuadrato(quadratoEsaminato);
//					for (int colonna : INDICI_02) {
//						if (colonneQuadrato.get(colonna).contains(numero)) {
//							colonneTernaOspitali.remove(new Integer(colonna));
//							quadratiOspitali.remove(new Integer(quadrato));
//						}
//					}
//				}
//				if (colonneTernaOspitali.size() == 1) {
//					List<Integer> quadratoOspitale = quadratiTerna.get(quadratiOspitali.get(0));
//					List<List<Integer>> colonneQuadratoOspitale = getColonneQuadrato(quadratoOspitale);
//					if (Collections.frequency(colonneQuadratoOspitale.get(colonneTernaOspitali.get(0)), 0) == 1) {
//						int indiceCarattere = colonneQuadratoOspitale.get(colonneTernaOspitali.get(0)).indexOf(0);
//						cambiamenti.add(
//								new Cambiamento(
//										((3 * quadratiOspitali.get(0)) + (indiceCarattere + 1)),
//										((3 * terzettoColonne) + (colonneTernaOspitali.get(0) + 1)), 
//										numero));
//					} else {
//						List<Integer> righeQuadratoOspitali = new ArrayList<>(INDICI_02);
//						int contoRighe = 0;
//						for (int cifra : colonneQuadratoOspitale.get(colonneTernaOspitali.get(0))) {
//							if (cifra != 0) {
//								righeQuadratoOspitali.remove(new Integer(contoRighe));
//							} else {
//								if (righe.get((3 * quadratiOspitali.get(0)) + contoRighe).contains(numero)) {
//									righeQuadratoOspitali.remove(new Integer(contoRighe));
//								}
//							}
//							contoRighe++;
//						}
//						if (righeQuadratoOspitali.size() == 1) {
//							cambiamenti.add(
//									new Cambiamento(
//											((3 * quadratiOspitali.get(0)) + righeQuadratoOspitali.get(0) + 1),
//											(3 * terzettoColonne + colonneTernaOspitali.get(0) + 1), 
//											numero));
//						}
//					}
//				}
//			}
//		}
//		return cambiamenti;
//	}
//
//	private List<Cambiamento> controlloRigheQuadrati() {
//		List<Cambiamento> cambiamenti = new ArrayList<>();
//		for (int quadrato : INDICI_08) {
//			List<Integer> quadratoEsaminato = quadrati.get(quadrato);
//			List<Integer> cifreQuadrato = getNumeriQuadrato(quadratoEsaminato);
//			List<Integer> cifreDaInserire = new ArrayList<>();
//			for (int cifra : CIFRE) {
//				if (!cifreQuadrato.contains(cifra)) {
//					cifreDaInserire.add(cifra);
//				}
//			}
//			List<List<Integer>> righeCostituentiQuadrato = terzettiRighe.get(quadrato / 3);
//			int variabile = quadrato;
//			while (variabile >= 3) {
//				variabile -= 3;
//			}
//			List<List<Integer>> colonneCostituentiQuadrato = terzettiColonne.get(variabile);
//			List<List<Integer>> righeQuadrato = getRigheQuadrato(quadratoEsaminato);
//			List<List<Integer>> colonneQuadrato = getColonneQuadrato(quadratoEsaminato);
//			for (int cifraDaInserire : cifreDaInserire) {
//				List<List<Integer>> righeQuadratoOspitali = new ArrayList<>();
//				int conto2 = 0;
//				List<Integer> indiciRighe = new ArrayList<>();
//				for (List<Integer> rigaCostituente : righeCostituentiQuadrato) {
//					if (Collections.frequency(righeQuadrato.get(conto2), 0) > 0) {
//						if (!rigaCostituente.contains(cifraDaInserire)) {
//							righeQuadratoOspitali.add(righeQuadrato.get(conto2));
//							indiciRighe.add(conto2);
//						}
//					}
//					conto2 += 1;
//				}
//				if (righeQuadratoOspitali.size() == 1) {
//					int coordinataRiga = indiciRighe.get(0) + 1;
//					List<List<Integer>> colonneQuadratoOspitali = new ArrayList<>();
//					int conto3 = 0;
//					List<Integer> indiciColonne = new ArrayList<>();
//					for (List<Integer> colonna : colonneQuadrato) {
//						int indiceDaControllare = indiciRighe.get(0);
//						if (colonna.get(indiceDaControllare) == 0) {
//							if (!colonneCostituentiQuadrato.get(conto3).contains(cifraDaInserire)) {
//								colonneQuadratoOspitali.add(colonneQuadrato.get(conto3));
//								indiciColonne.add(conto3);
//							}
//						}
//						conto3 += 1;
//					}
//					if (colonneQuadratoOspitali.size() == 1) {
//						int coordinataColonna = indiciColonne.get(0) + 1;
//						cambiamenti.add(new Cambiamento(coordinataRiga + (3 * (quadrato / 3)),
//								coordinataColonna + (3 * variabile), cifraDaInserire));
//					}
//				}
//			}
//		}
//		return cambiamenti;
//	}
//
//	private List<Cambiamento> controlloColonneQuadrati() {
//		List<Cambiamento> cambiamenti = new ArrayList<>();
//		for (int quadrato : INDICI_08) {
//			List<Integer> quadratoEsaminato = quadrati.get(quadrato);
//			List<Integer> cifreQuadrato = getNumeriQuadrato(quadratoEsaminato);
//			List<Integer> cifreDaInserire = new ArrayList<>();
//			for (int cifra : CIFRE) {
//				if (!cifreQuadrato.contains(cifra)) {
//					cifreDaInserire.add(cifra);
//				}
//			}
//			List<List<Integer>> righeCostituentiQuadrato = terzettiRighe.get(quadrato / 3);
//			int variabile = quadrato;
//			while (variabile >= 3) {
//				variabile -= 3;
//			}
//			List<List<Integer>> colonneCostituentiQuadrato = terzettiColonne.get(variabile);
//			List<List<Integer>> righeQuadrato = getRigheQuadrato(quadratoEsaminato);
//			List<List<Integer>> colonneQuadrato = getColonneQuadrato(quadratoEsaminato);
//			for (int cifraDaInserire : cifreDaInserire) {
//				List<List<Integer>> colonneQuadratoOspitali = new ArrayList<>();
//				int conto2 = 0;
//				List<Integer> indiciColonne = new ArrayList<>();
//				for (List<Integer> colonnaCostituente : colonneCostituentiQuadrato) {
//					if (Collections.frequency(colonneQuadrato.get(conto2), 0) > 0) {
//						if (!colonnaCostituente.contains(cifraDaInserire)) {
//							colonneQuadratoOspitali.add(righeQuadrato.get(conto2));
//							indiciColonne.add(conto2);
//						}
//					}
//					conto2 += 1;
//				}
//				if (colonneQuadratoOspitali.size() == 1) {
//					int coordinataColonna = indiciColonne.get(0) + 1;
//					List<List<Integer>> righeQuadratoOspitali = new ArrayList<>();
//					int conto3 = 0;
//					List<Integer> indiciRighe = new ArrayList<>();
//					for (List<Integer> riga : righeQuadrato) {
//						int indiceDaControllare = indiciColonne.get(0);
//						if (riga.get(indiceDaControllare) == 0) {
//							if (!righeCostituentiQuadrato.get(conto3).contains(cifraDaInserire)) {
//								righeQuadratoOspitali.add(righeQuadrato.get(conto3));
//								indiciRighe.add(conto3);
//							}
//						}
//						conto3 += 1;
//					}
//					if (righeQuadratoOspitali.size() == 1) {
//						int coordinataRiga = indiciRighe.get(0) + 1;
//						cambiamenti.add(new Cambiamento(coordinataRiga + (3 * (quadrato / 3)),
//								coordinataColonna + (3 * variabile), cifraDaInserire));
//					}
//				}
//			}
//		}
//		return cambiamenti;
//	}
//
//	private List<Cambiamento> controlloRigheExtreme() {
//		List<Cambiamento> cambiamenti = new ArrayList<>();
//		for (int numero : CIFRE) {
//			for (int terna : INDICI_02) {
//				List<Integer> righeTernaOspitali = new ArrayList<>(INDICI_02);
//				List<Integer> quadratiOspitali = new ArrayList<>(INDICI_02);
//				List<List<Integer>> quadratiTerna = getQuadratiTernaRighe(terna);
//				for (int quadrato : INDICI_02) {
//					List<Integer> quadratoTernaEsaminato = quadrati.get(quadrato + (3 * terna));
//					List<List<Integer>> righeQuadrato = getRigheQuadrato(quadratoTernaEsaminato);
//					List<Integer> righeQuadratoOspitali = new ArrayList<>(INDICI_02);
//					int contoRighe = 0;
//					for (List<Integer> riga : righeQuadrato) {
//						if (riga.contains(numero)) {
//							righeQuadratoOspitali.remove(new Integer(contoRighe));
//							quadratiOspitali.remove(new Integer(quadrato));
//							if (righeTernaOspitali.contains(contoRighe)) {
//								righeTernaOspitali.remove(new Integer(contoRighe));
//							}
//						} else {
//							if (righe.get((3 * terna) + contoRighe).contains(numero)) {
//								righeQuadratoOspitali.remove(new Integer(contoRighe));
//								if (righeTernaOspitali.contains(contoRighe)) {
//									righeTernaOspitali.remove(new Integer(contoRighe));
//								}
//							} else {
//								if (Collections.frequency(riga, 0) == 0) {
//									righeQuadratoOspitali.remove(new Integer(contoRighe));
//								} else {
//									List<Integer> elementiOspitali = new ArrayList<>(INDICI_02);
//									int contoElementi = 0;
//									for (int elemento : riga) {
//										if (elemento != 0) {
//											elementiOspitali.remove(new Integer(contoElementi));
//										} else {
//											if (colonne.get((3 * quadrato) + contoElementi).contains(numero)) {
//												elementiOspitali.remove(new Integer(contoElementi));
//											}
//										}
//										contoElementi++;
//									}
//									if (elementiOspitali.isEmpty()) {
//										righeQuadratoOspitali.remove(new Integer(contoRighe));
//									}
//								}
//							}
//						}
//						contoRighe++;
//					}
//					if (righeQuadratoOspitali.size() == 1) {
//						if (!quadratoTernaEsaminato.contains(numero)) {
//							if (righeTernaOspitali.size() > 1) {
//								if (righeTernaOspitali.contains(righeQuadratoOspitali.get(0))) {
//									// AGGIUNTA IMPORTANTISSIMA PER EVITARE BUG IN TENTATIVO5050
//									righeTernaOspitali.remove(righeQuadratoOspitali.get(0));
//								}
//							}
//							if (quadratiOspitali.contains(quadrato)) {
//								quadratiOspitali.remove(new Integer(quadrato));
//							}
//						}
//					}
//				}
//				if (quadratiOspitali.size() == 1) {
//					List<Integer> quadratoLibero = quadratiTerna.get(quadratiOspitali.get(0));
//					List<List<Integer>> righeQuadratoLibero = getRigheQuadrato(quadratoLibero);
//					if (Collections.frequency(righeQuadratoLibero.get(righeTernaOspitali.get(0)), 0) == 1) {
//						int indiceColonna = righeQuadratoLibero.get(righeTernaOspitali.get(0)).indexOf(0);
//						cambiamenti.add(
//								new Cambiamento(
//										(3 * terna) + (righeTernaOspitali.get(0) + 1),
//										(3 * quadratiOspitali.get(0)) + (indiceColonna + 1), 
//										numero));
//					} else {
//						List<Integer> colonneQuadratoOspitali = new ArrayList<>(INDICI_02);
//						int contoColonne = 0;
//						for (int elemento : righeQuadratoLibero.get(righeTernaOspitali.get(0))) {
//							if (elemento != 0) {
//								colonneQuadratoOspitali.remove(new Integer(contoColonne));
//							} else {
//								if (colonne.get(contoColonne + (3 * quadratiOspitali.get(0))).contains(numero)) {
//									colonneQuadratoOspitali.remove(new Integer(contoColonne));
//								}
//							}
//							contoColonne++;
//						}
//						if (colonneQuadratoOspitali.size() == 1) {
//							cambiamenti.add(
//									new Cambiamento(
//											(righeTernaOspitali.get(0) + 1) + (3 * terna),
//											(colonneQuadratoOspitali.get(0) + 1) + (3 * quadratiOspitali.get(0)), 
//											numero));
//						}
//					}
//				}
//			}
//		}
//		return cambiamenti;
//	}
//
//	private List<Cambiamento> controlloColonneExtreme() {
//		List<Cambiamento> cambiamenti = new ArrayList<>();
//		for (int numero : CIFRE) {
//			for (int terna : INDICI_02) {
//				List<Integer> colonneTernaOspitali = new ArrayList<>(INDICI_02);
//				List<Integer> quadratiOspitali = new ArrayList<>(INDICI_02);
//				List<List<Integer>> quadratiTerna = getQuadratiTernaColonne(terna);
//				for (int quadrato : INDICI_02) {
//					List<Integer> quadratoTernaEsaminato = quadrati.get(terna + (3 * quadrato));
//					List<List<Integer>> colonneQuadrato = getColonneQuadrato(quadratoTernaEsaminato);
//					List<Integer> colonneQuadratoOspitali = new ArrayList<>(INDICI_02);
//					int contoColonne = 0;
//					for (List<Integer> colonna : colonneQuadrato) {
//						if (colonna.contains(numero)) {
//							colonneQuadratoOspitali.remove(new Integer(contoColonne));
//							quadratiOspitali.remove(new Integer(quadrato));
//							if (colonneTernaOspitali.contains(contoColonne)) {
//								colonneTernaOspitali.remove(new Integer(contoColonne));
//							}
//						} else {
//							if (colonne.get((3 * terna) + contoColonne).contains(numero)) {
//								colonneQuadratoOspitali.remove(new Integer(contoColonne));
//								if (colonneTernaOspitali.contains(contoColonne)) {
//									colonneTernaOspitali.remove(new Integer(contoColonne));
//								}
//							} else {
//								if (Collections.frequency(colonna, 0) == 0) {
//									colonneQuadratoOspitali.remove(new Integer(contoColonne));
//								} else {
//									List<Integer> elementiOspitali = new ArrayList<>(INDICI_02);
//									int contoElementi = 0;
//									for (int elemento : colonna) {
//										if (elemento != 0) {
//											elementiOspitali.remove(new Integer(contoElementi));
//										} else {
//											if (righe.get((3 * quadrato) + contoElementi).contains(numero)) {
//												elementiOspitali.remove(new Integer(contoElementi));
//											}
//										}
//										contoElementi++;
//									}
//									if (elementiOspitali.isEmpty()) {
//										colonneQuadratoOspitali.remove(new Integer(contoColonne));
//									}
//								}
//							}
//						}
//						contoColonne++;
//					}
//					if (colonneQuadratoOspitali.size() == 1) {
//						if (!quadratoTernaEsaminato.contains(numero)) {
//							if (colonneTernaOspitali.size() > 1) {
//								if (colonneTernaOspitali.contains(colonneQuadratoOspitali.get(0))) {
//									// AGGIUNTA IMPORTANTISSIMA PER EVITARE BUG IN TENTATIVO5050
//									colonneTernaOspitali.remove(colonneQuadratoOspitali.get(0));
//								}
//							}
//							if (quadratiOspitali.contains(quadrato)) {
//								quadratiOspitali.remove(new Integer(quadrato));
//							}
//						}
//					}
//				}
//				if (quadratiOspitali.size() == 1) {
//					List<Integer> quadratoLibero = quadratiTerna.get(quadratiOspitali.get(0));
//					List<List<Integer>> colonneQuadratoLibero = getColonneQuadrato(quadratoLibero);
//					if (Collections.frequency(colonneQuadratoLibero.get(colonneTernaOspitali.get(0)), 0) == 1) {
//						int indiceRiga = colonneQuadratoLibero.get(colonneTernaOspitali.get(0)).indexOf(0);
//						cambiamenti.add(
//								new Cambiamento(
//										(3 * quadratiOspitali.get(0)) + (indiceRiga + 1),
//										(3 * terna) + (colonneTernaOspitali.get(0) + 1), 
//										numero));
//					} else {
//						List<Integer> righeQuadratoOspitali = new ArrayList<>(INDICI_02);
//						int contoRighe = 0;
//						for (int elemento : colonneQuadratoLibero.get(colonneTernaOspitali.get(0))) {
//							if (elemento != 0) {
//								righeQuadratoOspitali.remove(new Integer(contoRighe));
//							} else {
//								if (righe.get((3 * quadratiOspitali.get(0)) + contoRighe).contains(numero)) {
//									righeQuadratoOspitali.remove(new Integer(contoRighe));
//								}
//							}
//							contoRighe++;
//						}
//						if (righeQuadratoOspitali.size() == 1) {
//							cambiamenti.add(
//									new Cambiamento(
//											(3 * quadratiOspitali.get(0)) + (righeQuadratoOspitali.get(0) + 1),
//											(3 * terna) + (colonneTernaOspitali.get(0) + 1), 
//											numero));
//						}
//					}
//				}
//			}
//		}
//		return cambiamenti;
//	}

//	private static Sudoku getRandomSudoku() {
//	Random random = new Random();
//	List<Sudoku> tutti = getAllSudoku();
//	return tutti.get(random.nextInt(tutti.size()));
//}

//	private List<tab> getTabsOspitaliQuadrato(Integer numeroQuadrato, Integer numero, List<tab> tabs) {
//		List<Integer> righeQuadrato = null;
//		List<Integer> colonneQuadrato = null;
//		switch (numeroQuadrato) {
//		case 0:
//			righeQuadrato = new ArrayList<>(Arrays.asList(1, 2, 3));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(1, 2, 3));
//			break;
//		case 1:
//			righeQuadrato = new ArrayList<>(Arrays.asList(1, 2, 3));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(4, 5, 6));
//			break;
//		case 2:
//			righeQuadrato = new ArrayList<>(Arrays.asList(1, 2, 3));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(7, 8, 9));
//			break;
//		case 3:
//			righeQuadrato = new ArrayList<>(Arrays.asList(4, 5, 6));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(1, 2, 3));
//			break;
//		case 4:
//			righeQuadrato = new ArrayList<>(Arrays.asList(4, 5, 6));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(4, 5, 6));
//			break;
//		case 5:
//			righeQuadrato = new ArrayList<>(Arrays.asList(4, 5, 6));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(7, 8, 9));
//			break;
//		case 6:
//			righeQuadrato = new ArrayList<>(Arrays.asList(7, 8, 9));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(1, 2, 3));
//			break;
//		case 7:
//			righeQuadrato = new ArrayList<>(Arrays.asList(7, 8, 9));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(4, 5, 6));
//			break;
//		case 8:
//			righeQuadrato = new ArrayList<>(Arrays.asList(7, 8, 9));
//			colonneQuadrato = new ArrayList<>(Arrays.asList(7, 8, 9));
//			break;
//		}
//		List<tab> tabsOspitali = new ArrayList<>();
//		for (tab tab : tabs) {
//			if (righeQuadrato.contains(tab.getRiga()) 
//			&& colonneQuadrato.contains(tab.getColonna()) 
//			&& tab.getNumeri().contains(numero)) {
//				tabsOspitali.add(tab);
//			}
//		}
//		return tabsOspitali;
//	}

//	private List<tab> getTabsOspitaliRiga(Integer numeroRiga, Integer numero, List<tab> tabs) {
//		List<tab> tabsOspitali = new ArrayList<>();
//		for (tab tab : tabs) {
//			if (tab.getRiga() == numeroRiga 
//			&& tab.getNumeri().contains(numero)) {
//				tabsOspitali.add(tab);
//			}
//		}
//		return tabsOspitali;
//	}

//	private List<tab> getTabsOspitaliColonna(Integer numeroColonna, Integer numero, List<tab> tabs) {
//		List<tab> tabsOspitali = new ArrayList<>();
//		for (tab tab : tabs) {
//			if (tab.getColonna() == numeroColonna && tab.getNumeri().contains(numero)) {
//				tabsOspitali.add(tab);
//			}
//		}
//		return tabsOspitali;
//	}

    // PER TUTTI I METODI SEGUENTI, GLI INDICI DEI PARAMETRI PARTONO DA 1, NON DA 0
//	private List<Integer> getNumeriRiga(int indiceRiga) {
//		List<Integer> numeriRiga = new ArrayList<>();
//		for (int numero : righe.get(indiceRiga - 1)) {
//			if (numero != 0) {
//				numeriRiga.add(numero);
//			}
//		}
//		return numeriRiga;
//	}
//
//	private List<Integer> getNumeriMancantiRiga(int indiceRiga) {
//		List<Integer> numeriRiga = getNumeriRiga(indiceRiga);
//		List<Integer> numeriMancantiRiga = new ArrayList<>();
//		for (int numero : CIFRE) {
//			if (!numeriRiga.contains(numero)) {
//				numeriMancantiRiga.add(numero);
//			}
//		}
//		return numeriMancantiRiga;
//	}
//
//	private List<Integer> getNumeriColonna(int indiceColonna) {
//		List<Integer> numeriColonna = new ArrayList<>();
//		for (int numero : colonne.get(indiceColonna - 1)) {
//			if (numero != 0) {
//				numeriColonna.add(numero);
//			}
//		}
//		return numeriColonna;
//	}
//
//	private List<Integer> getNumeriQuadrato(int indiceQuadrato) {
//		List<Integer> numeriQuadrato = new ArrayList<>();
//		List<Integer> quadratoEsaminato = quadrati.get(indiceQuadrato - 1);
//		for (int numero : quadratoEsaminato) {
//			if (numero != 0) {
//				numeriQuadrato.add(numero);
//			}
//		}
//		return numeriQuadrato;
//	}
}

//private static RisultatoScrematura hiddenSingleRow(Sudoku sudoku, List<tab> tabs) {
//	List<Cambiamento> cambiamenti = new ArrayList<>();
//	for (int riga : Utils.INDICI_08) {
//		for (int numero : Utils.CIFRE) {
//			if (!sudoku.getRows().get(riga).contains(numero)) {
//				List<tab> tabsOspitali = new ArrayList<>();
//				for (tab tab : tabs) {
//					if (tab.getRow() == riga && tab.getNumbers().contains(numero)) {
//						tabsOspitali.add(tab);
//					} 
//				}
//				if (tabsOspitali.size() == 1) {
//					tab tab = tabsOspitali.get(0);
//					Cambiamento cambiamento = new Cambiamento(HIDDEN_SINGLE, Cambiamento.ROW, tab.getRow(), tab.getCol(), numero);
//					cambiamenti.add(cambiamento);
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<>(cambiamenti), tabs);
//}
//
//private static RisultatoScrematura hiddenSingleCol(Sudoku sudoku, List<tab> tabs) {
//	List<Cambiamento> cambiamenti = new ArrayList<>();
//	for (int colonna : Utils.INDICI_08) {
//		for (int numero : Utils.CIFRE) {
//			if (!sudoku.getColumns().get(colonna).contains(numero)) {
//				List<tab> tabsOspitali = new ArrayList<>();
//				for (tab tab : tabs) {
//					if (tab.getCol() == colonna && tab.getNumbers().contains(numero)) {
//						tabsOspitali.add(tab);
//					} 
//				}
//				if (tabsOspitali.size() == 1) {
//					tab tab = tabsOspitali.get(0);
//					Cambiamento cambiamento = new Cambiamento(HIDDEN_SINGLE, Cambiamento.COL, tab.getRow(), tab.getCol(), numero);
//					cambiamenti.add(cambiamento);
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<>(cambiamenti), tabs);
//}

//private static RisultatoScrematura nakedPairsRow(List<tab> tabs) {
//	Set<Scrematura> scremature = new HashSet<>();
//	for (int riga : Utils.CIFRE) {
//		Set<List<Integer>> coppieSet = new HashSet<>();
//		List<List<Integer>> coppieList = new ArrayList<>();
//		List<tab> tabUnit = Utils.getHouseTabs(Cambiamento.ROW, riga, tabs);
//		for (tab tab : tabUnit) {
//			if (tab.getNumbers().size() == 2) {
//				coppieSet.add(tab.getNumbers());
//				coppieList.add(tab.getNumbers());
//			}
//		}
//		List<List<Integer>> coppie = new ArrayList<>(coppieSet);
//		for (List<Integer> coppia : coppie) {
//			if (Collections.frequency(coppieList, coppia) == 2) {
//				List<tab> tabUnit2 = Utils.getHouseTabs(Cambiamento.ROW, riga, tabs);
//				for (tab tab : tabUnit2) {
//					if (!tab.getNumbers().equals(coppia)) {
//						List<Integer> candidatiCoppiaRimossi = new ArrayList<>();
//						for (Integer numero : coppia) {
//							if (tab.getNumbers().remove(numero)) {
//								candidatiCoppiaRimossi.add(numero);
//							}
//						}
//						if (!candidatiCoppiaRimossi.isEmpty()) {
//							scremature.add(new Scrematura(NAKED_PAIRS, Cambiamento.ROW, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiCoppiaRimossi));
//							//
////							System.out.println("NAKED PAIR ROW: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Cambiamento>(scremature), tabs);
//}
//
//private static RisultatoScrematura nakedPairsCol(List<tab> tabs) {
//	Set<Scrematura> scremature = new HashSet<>();
//	for (int colonna : Utils.INDICI_08) {
//		Set<List<Integer>> coppieSet = new HashSet<>();
//		List<List<Integer>> coppieList = new ArrayList<>();
//		List<tab> tabUnit = Utils.getHouseTabs(Cambiamento.COL, colonna, tabs);
//		for (tab tab : tabUnit) {
//			if (tab.getNumbers().size() == 2) {
//				coppieSet.add(tab.getNumbers());
//				coppieList.add(tab.getNumbers());
//			}
//		}
//		List<List<Integer>> coppie = new ArrayList<>(coppieSet);
//		for (List<Integer> coppia : coppie) {
//			if (Collections.frequency(coppieList, coppia) == 2) {
//				List<tab> tabUnit2 = Utils.getHouseTabs(Cambiamento.COL, colonna, tabs);
//				for (tab tab : tabUnit2) {
//					if (!tab.getNumbers().equals(coppia)) {
//						List<Integer> candidatiCoppiaRimossi = new ArrayList<>();
//						for (Integer numero : coppia) {
//							if (tab.getNumbers().remove(numero)) {
//								candidatiCoppiaRimossi.add(numero);
//							}
//						}
//						if (!candidatiCoppiaRimossi.isEmpty()) {
//							scremature.add(new Scrematura(NAKED_PAIRS, Cambiamento.COL, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiCoppiaRimossi));
//							//
////							System.out.println("NAKED PAIR COL: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Cambiamento>(scremature), tabs);
//}

//private static RisultatoScrematura nakedTriplesRow(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int riga : Utils.INDICI_08) {
//		Set<List<Integer>> triple = new HashSet<>();
//		for (tab tab : tabs) {
//			if (tab.getRow() == riga && tab.getNumbers().size() == 3) {
//				triple.add(tab.getNumbers());
//			}
//		}
//		for (List<Integer> tripla : triple) {
//			List<tab> membriTripla = new ArrayList<>();
//			for (tab tab : tabs) {
//				if (tab.getRow() == riga && candidatesAreSameOrSubset(tab, tripla)) {
//					membriTripla.add(tab);
//				}
//			}
//			if (membriTripla.size() == 3) {
//				for (tab tab : tabs) {
//					if (tab.getRow() == riga && !membriTripla.contains(tab)) {
//						List<Integer> candidatiTriplaRimossi = new ArrayList<>();
//						for (Integer numero : tripla) {
//							if (tab.getNumbers().remove(numero)) {
//								candidatiTriplaRimossi.add(numero);
//							}
//						}
//						if (!candidatiTriplaRimossi.isEmpty()) {
//							scremature.add(new Skimming(NAKED_TRIPLE, Change.ROW, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiTriplaRimossi));
//							//
////							System.out.println("NAKED TRIPLE ROW: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}
//
//private static RisultatoScrematura nakedTriplesCol(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int colonna : Utils.INDICI_08) {
//		Set<List<Integer>> triple = new HashSet<>();
//		for (tab tab : tabs) {
//			if (tab.getCol() == colonna && tab.getNumbers().size() == 3) {
//				triple.add(tab.getNumbers());
//			}
//		}
//		for (List<Integer> tripla : triple) {
//			List<tab> membriTripla = new ArrayList<>();
//			for (tab tab : tabs) {
//				if (tab.getCol() == colonna && candidatesAreSameOrSubset(tab, tripla)) {
//					membriTripla.add(tab);
//				}
//			}
//			if (membriTripla.size() == 3) {
//				for (tab tab : tabs) {
//					if (tab.getCol() == colonna && !membriTripla.contains(tab)) {
//						List<Integer> candidatiTriplaRimossi = new ArrayList<>();
//						for (Integer numero : tripla) {
//							if (tab.getNumbers().remove(numero)) {
//								candidatiTriplaRimossi.add(numero);
//							}
//						}
//						if (!candidatiTriplaRimossi.isEmpty()) {
//							scremature.add(new Skimming(NAKED_TRIPLE, Change.COL, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiTriplaRimossi));
//							//
////							System.out.println("NAKED TRIPLE COL: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}

//private static RisultatoScrematura hiddenTriplesRow(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int riga : Utils.CIFRE) {
//		List<Integer> candidatesWithAtLeastTwoOccurences = new ArrayList<>();
//		for (int numero : Utils.CIFRE) {
//			int occorrenze = 0;
//			List<tab> tabUnit = Utils.getHouseTabs(Change.ROW, riga, tabs);
//			for (tab tab : tabUnit) {
//				if (tab.getNumbers().contains(numero)) {
//					occorrenze++;
//				}
//			}
//			if (occorrenze == 2 || occorrenze == 3) {
//				candidatesWithAtLeastTwoOccurences.add(numero);
//			}
//		}
//		if (candidatesWithAtLeastTwoOccurences.size() >= 3) {
//			List<List<Integer>> possibleTriples = findAllPossibleTriples(candidatesWithAtLeastTwoOccurences);
//			for (List<Integer> possibleTriple : possibleTriples) {
//				List<tab> tripleTabs = new ArrayList<>();
//				List<tab> impostoriTabs = new ArrayList<>();
//				List<tab> tabUnit = Utils.getHouseTabs(Change.ROW, riga, tabs);
//				for (tab tab : tabUnit) {
//					if (containsAtLeastOneCandidate(tab.getNumbers(), possibleTriple)) {
//						if (containsAtLeastTwoCandidates(tab.getNumbers(), possibleTriple)) {
//							tripleTabs.add(tab);
//						} else {
//							impostoriTabs.add(tab);
//						}
//					}
//				}
//				if (tripleTabs.size() == 3 && impostoriTabs.isEmpty()) {
//					for (tab tab : tripleTabs) {
//						List<Integer> candidatiDaRimuovere = new ArrayList<>();
//						for (int candidato : tab.getNumbers()) {
//							if (!possibleTriple.contains(candidato)) {
//								candidatiDaRimuovere.add(candidato);
//							}
//						}
//						tab.getNumbers().removeAll(candidatiDaRimuovere);
//						if (!candidatiDaRimuovere.isEmpty()) {
//							scremature.add(new Skimming(HIDDEN_TRIPLES, Change.ROW, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiDaRimuovere));
//							//
////							System.out.println("HIDDEN TRIPLES ROW: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}
//
//private static RisultatoScrematura hiddenTriplesCol(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int colonna : Utils.CIFRE) {
//		List<Integer> candidatesWithAtLeastTwoOccurences = new ArrayList<>();
//		for (int numero : Utils.CIFRE) {
//			int occorrenze = 0;
//			List<tab> tabUnit = Utils.getHouseTabs(Change.COL, colonna, tabs);
//			for (tab tab : tabUnit) {
//				if (tab.getNumbers().contains(numero)) {
//					occorrenze++;
//				}
//			}
//			if (occorrenze == 2 || occorrenze == 3) {
//				candidatesWithAtLeastTwoOccurences.add(numero);
//			}
//		}
//		if (candidatesWithAtLeastTwoOccurences.size() >= 3) {
//			List<List<Integer>> possibleTriples = findAllPossibleTriples(candidatesWithAtLeastTwoOccurences);
//			for (List<Integer> possibleTriple : possibleTriples) {
//				List<tab> tripleTabs = new ArrayList<>();
//				List<tab> impostoriTabs = new ArrayList<>();
//				List<tab> tabUnit = Utils.getHouseTabs(Change.COL, colonna, tabs);
//				for (tab tab : tabUnit) {
//					if (containsAtLeastOneCandidate(tab.getNumbers(), possibleTriple)) {
//						if (containsAtLeastTwoCandidates(tab.getNumbers(), possibleTriple)) {
//							tripleTabs.add(tab);
//						} else {
//							impostoriTabs.add(tab);
//						}
//					}
//				}
//				if (tripleTabs.size() == 3 && impostoriTabs.isEmpty()) {
//					for (tab tab : tripleTabs) {
//						List<Integer> candidatiDaRimuovere = new ArrayList<>();
//						for (int candidato : tab.getNumbers()) {
//							if (!possibleTriple.contains(candidato)) {
//								candidatiDaRimuovere.add(candidato);
//							}
//						}
//						tab.getNumbers().removeAll(candidatiDaRimuovere);
//						if (!candidatiDaRimuovere.isEmpty()) {
//							scremature.add(new Skimming(HIDDEN_TRIPLES, Change.COL, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiDaRimuovere));
//							//
////							System.out.println("HIDDEN TRIPLES COL: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}

//private static RisultatoScrematura nakedQuadruplesRow(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int riga : Utils.INDICI_08) {
//		Set<List<Integer>> quadruple = new HashSet<>();
//		for (tab tab : tabs) {
//			if (tab.getRow() == riga && tab.getNumbers().size() == 4) {
//				quadruple.add(tab.getNumbers());
//			}
//		}
//		for (List<Integer> quadrupla : quadruple) {
//			List<tab> membriQuadrupla = new ArrayList<>();
//			for (tab tab : tabs) {
//				if (tab.getRow() == riga && Utils.candidatesAreSameOrSubset(tab, quadrupla)) {
//					membriQuadrupla.add(tab);
//				}
//			}
//			if (membriQuadrupla.size() == 4) {
//				for (tab tab : tabs) {
//					if (tab.getRow() == riga && !membriQuadrupla.contains(tab)) {
//						List<Integer> candidatiQuadruplaRimossi = new ArrayList<>();
//						for (Integer numero : quadrupla) {
//							if (tab.getNumbers().remove(numero)) {
//								candidatiQuadruplaRimossi.add(numero);
//							}
//						}
//						if (!candidatiQuadruplaRimossi.isEmpty()) {
//							scremature.add(new Skimming(NAKED_QUADRUPLE, Change.ROW, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiQuadruplaRimossi));
//							//
////							System.out.println("NAKED QUADRUPLE ROW: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}
//
//private static RisultatoScrematura nakedQuadruplesCol(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int colonna : Utils.INDICI_08) {
//		Set<List<Integer>> quadruple = new HashSet<>();
//		for (tab tab : tabs) {
//			if (tab.getCol() == colonna && tab.getNumbers().size() == 4) {
//				quadruple.add(tab.getNumbers());
//			}
//		}
//		for (List<Integer> quadrupla : quadruple) {
//			List<tab> membriQuadrupla = new ArrayList<>();
//			for (tab tab : tabs) {
//				if (tab.getCol() == colonna && Utils.candidatesAreSameOrSubset(tab, quadrupla)) {
//					membriQuadrupla.add(tab);
//				}
//			}
//			if (membriQuadrupla.size() == 4) {
//				for (tab tab : tabs) {
//					if (tab.getCol() == colonna && !membriQuadrupla.contains(tab)) {
//						List<Integer> candidatiQuadruplaRimossi = new ArrayList<>();
//						for (Integer numero : quadrupla) {
//							if (tab.getNumbers().remove(numero)) {
//								candidatiQuadruplaRimossi.add(numero);
//							}
//						}
//						if (!candidatiQuadruplaRimossi.isEmpty()) {
//							scremature.add(new Skimming(NAKED_QUADRUPLE, Change.COL, tab.getRow(), tab.getCol(), tab.getNumbers(), candidatiQuadruplaRimossi));
//							//
////							System.out.println("NAKED QUADRUPLE COL: ho rimosso " + candidatiDaRimuovere + " dal tab " + tab);
//							//
//						}
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}

//private static RisultatoScrematura lockedCandidatesType2Col(List<tab> tabs) {
//	Set<Skimming> scremature = new HashSet<>();
//	for (int colonna : Utils.INDICI_08) {
//		for (int numero : Utils.NUMBERS) {
//			List<tab> tabsOspitali = new ArrayList<>();
//			Set<Integer> quadratitabsOspitali = new HashSet<>();
//			for (tab tab : tabs) {
//				if (tab.getCol() == colonna && tab.getNumbers().contains(numero)) {
//					tabsOspitali.add(tab);
//					quadratitabsOspitali.add(tab.getBox());
//				}
//			}
//			if (quadratitabsOspitali.size() == 1) {
//				int indiceQuadrato = new ArrayList<>(quadratitabsOspitali).get(0); 
//				for (tab tab : tabs) {
//					if (tab.getCol() != colonna && tab.getBox() == indiceQuadrato && tab.getNumbers().contains(numero)) {
//						///
////						System.out.println("LOCKED 2 COLONNE: IL NUMERO " + numero + " NON PUO' ANDARE NELLA CASELLA " + tab.getRow() + ", " + tab.getCol() + " IN QUANTO VA PER FORZA NELLA COLONNA " + (colonna + 1));
//						///
//						tab.getNumbers().remove(new Integer(numero));
//						Skimming s = new Skimming(LOCKED_CANDIDATE_TYPE_2, Change.COL, tab.getRow(), tab.getCol(), tab.getNumbers(), Arrays.asList(numero));
//						scremature.add(s);
//					}
//				}
//			}
//		}
//	}
//	return new RisultatoScrematura(new ArrayList<Change>(scremature), tabs);
//}