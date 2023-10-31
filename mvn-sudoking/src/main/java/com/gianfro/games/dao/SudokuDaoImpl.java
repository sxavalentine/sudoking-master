package com.gianfro.games.dao;

import com.gianfro.games.entities.SolutionOutput;
import com.gianfro.games.entities.Sudoku;
import com.gianfro.games.exceptions.NoFiftyFiftyException;
import com.gianfro.games.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SudokuDaoImpl implements SudokuDao {

    @Override
    public boolean salvaNelDB(SolutionOutput s) {
        Connection con = null;
        PreparedStatement pS = null;
        ResultSet rS = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GestioneOrdini?verifyServerCertificate=false&useSSL=true", "root", "root");
            pS = con.prepareStatement(
                    "INSERT INTO SUDOKING.SUDOKU_RISOLTI" +
                            "(startingNumbers, solutionNumbers, initialDigits, stepsCount, solutionTime) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pS.setString(1, s.getStartingNumbers());
            pS.setString(2, s.getSolutionNumbers());
            pS.setInt(3, s.getInitialDigits());
            pS.setInt(4, s.getStepsCount());
            pS.setInt(5, s.getSolutionTime());
            pS.executeUpdate();
            rS = pS.getGeneratedKeys();
            rS.next();
        } catch (SQLIntegrityConstraintViolationException eccSql) {
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println(s.getStartingNumbers());
            System.out.println("Eccezione da Sudoku.salvaNelDB: salvataggio non riuscito in quanto il sudoku risulta gia' presente " + eccSql.getMessage());
            return true;
        } catch (Exception ecc) {
            System.out.println("Eccezione da Sudoku.salvaNelDB: non sono riuscito a connettermi al DB " + ecc.getMessage());
            return false;
        } finally {
            try {
                if (rS != null) {
                    rS.close();
                }
                if (pS != null) {
                    pS.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ecc) {
                System.out.println("Eccezione da Sudoku.salvaNelDB: problemi nella chiusura della connessione al DB " + ecc.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean salvaErroreNelDB(Sudoku sudoku, NoFiftyFiftyException nffe) {
        Connection con = null;
        PreparedStatement pS = null;
        ResultSet rS = null;

        String startingNumbers = sudoku.getStringNumbers();
        String impasseNumbers = nffe.getSudokuAtTheTimeOfException().getStringNumbers();
        int initialDigits = 81 - StringUtils.countMatches(startingNumbers, "0");
        int impasseDigits = 81 - StringUtils.countMatches(impasseNumbers, "0");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GestioneOrdini?verifyServerCertificate=false&useSSL=true", "root", "root");
            pS = con.prepareStatement(
                    "INSERT INTO SUDOKING.SUDOKU_BLOCCATI" +
                            "(startingNumbers, impasseNumbers, initialDigits, impasseDigits, exceptionMessage) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pS.setString(1, startingNumbers);
            pS.setString(2, impasseNumbers);
            pS.setInt(3, initialDigits);
            pS.setInt(4, impasseDigits);
            pS.setString(5, nffe.getMessage());
            pS.executeUpdate();
            rS = pS.getGeneratedKeys();
            rS.next();
        } catch (SQLIntegrityConstraintViolationException eccSql) {
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println(startingNumbers);
            System.out.println("Eccezione da Sudoku.salvaErroreNelDB: salvataggio non riuscito in quanto il sudoku risulta gia' presente " + eccSql.getMessage());
            return true;
        } catch (Exception ecc) {
            System.out.println("Eccezione da Sudoku.salvaErroreNelDB: non sono riuscito a connettermi al DB " + ecc.getMessage());
            return false;
        } finally {
            try {
                if (rS != null) {
                    rS.close();
                }
                if (pS != null) {
                    pS.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ecc) {
                System.out.println("Eccezione da Sudoku.salvaErroreNelDB: problemi nella chiusura della connessione al DB " + ecc.getMessage());
                return false;
            }
        }
        return true;
    }

    public List<Sudoku> getSudokuRisolti() {
        Connection con = null;
        Statement s = null;
        ResultSet rS = null;
        List<Sudoku> sudokuRisolti = new LinkedList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GestioneOrdini?verifyServerCertificate=false&useSSL=true", "root", "root");
            s = con.createStatement();
            String query = "SELECT solutionNumbers FROM SUDOKING.SUDOKU_RISOLTI";
            rS = s.executeQuery(query);
            while (rS.next()) {
                sudokuRisolti.add(Utils.buildSudoku(rS.getString(1)));
            }
        } catch (Exception ecc) {
            System.out.println("Eccezione da Sudoku.getSudokuRisolti: non sono riuscito a connettermi al DB " + ecc.getMessage());
        } finally {
            try {
                if (rS != null) {
                    rS.close();
                }
                if (s != null) {
                    s.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ecc) {
                System.out.println("Eccezione da Sudoku.getSudokuRisolti: problemi nella chiusura della connessione al DB " + ecc.getMessage());
            }
        }
        return sudokuRisolti;
    }

    // con true restituisce i numeri iniziali, con false quelli al momento del blocco
    public List<Sudoku> getSudokuBloccati(boolean startOrStuck) {
        Connection con = null;
        Statement s = null;
        ResultSet rS = null;
        List<Sudoku> sudokuBloccati = new LinkedList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GestioneOrdini?verifyServerCertificate=false&useSSL=true", "root", "root");
            s = con.createStatement();
            String query = startOrStuck ? "SELECT startingNumbers FROM SUDOKING.SUDOKU_BLOCCATI" : "SELECT impasseNumbers FROM SUDOKING.SUDOKU_BLOCCATI";
            rS = s.executeQuery(query);
            while (rS.next()) {
                sudokuBloccati.add(Utils.buildSudoku(rS.getString(1)));
            }
        } catch (Exception ecc) {
            System.out.println("Eccezione da Sudoku.getSudokuBloccati: non sono riuscito a connettermi al DB " + ecc.getMessage());
        } finally {
            try {
                if (rS != null) {
                    rS.close();
                }
                if (s != null) {
                    s.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ecc) {
                System.out.println("Eccezione da Sudoku.getSudokuBloccati: problemi nella chiusura della connessione al DB " + ecc.getMessage());
            }
        }
        return sudokuBloccati;
    }
}
