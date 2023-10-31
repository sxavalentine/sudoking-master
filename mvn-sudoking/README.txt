SudokuController chiama il metodo solveSudoku di SudokuService
SudokuService chiama il metodo getSolution di SudokuSolver per ottenere un SolutionOutput

La collezione di chiamate Postman con cui testare l'applicativo si può trovare al seguente link:
https://winter-zodiac-86017.postman.co/workspace/CLZ~0f499c32-aec5-41be-ab6c-c30b8f49520f/collection/9138070-d3fc34a9-2ae7-4afd-98e8-ced0c687d838?action=share&creator=9138070

------------------------------------------ SolutionOutput ---------------------------------------------------------------------
Entità che rappresenta la soluzione di un Sudoku.
Contiene i seguenti campi:
- numeri iniziali sudoku
- numeri finali sudoku (una volta risolto)
- millisecondi impiegati a risolverlo
- numero di cifre iniziali del sudoku
- numero di passaggi per la sua risoluzione
- lista di SolutionStep per risolverlo (IMPORTANTISSIMA)

Il metodo getSolution costruisce il SolutionOutput invocando il metodo privato solve di SudokuSolver per produrre la lista di SolutionStep.

-------------------------------------------- SolutionStep --------------------------------------------------------------------
Entità che rappresenta un passaggio risolutivo di un Sudoku data la sua configurazione attuale
(numeri presenti al momento e tabulati delle celle ancora da riempire), elenca tutte le deduzioni che si possono fare su quell'istanza.


Un SolutionStep contiene i seguenti campi:
- sudokuInstance --> l'oggetto Sudoku (con relative proprietà) di quel passaggio
- sudokuNumbers  --> la stringa contenente la sequenza numerica del Sudoku in quello specifico passaggio
- changeLogs     --> lista di ChangeLogs (verranno meglio definiti più sotto)
- changes        --> lista di Changes (verranno meglio definiti più sotto)
- tabs           --> lista di Tab (verranno meglio definiti più sotto)

--------------------------------------------- ChangeLog ---------------------------------------------------------------------

Entità che rappresenta un insieme di uno o più cambiamenti da apportare al Sudoku in seguito a una deduzione.
Contiene i seguenti campi:
- unitExamined              --> lista di numeri (più frequentemente sarà una lista con un solo elemento) esaminati per quella deduzione
- house                     --> la casa (riga, colonna o box) presa in considerazione per tale deduzione. Può essere null (tecnica Naked1 ad esempio)
- houseNumber               --> il numero di tale casa (es: riga 2, colonna 4, box 9)
- unitMembers               --> lista di ChangeLogUnitMember che sono stati esaminati per quella deduzione (meglio definiti più sotto)
- solvingTechnique          --> la tecnica risolutiva usata per tale deduzione
- solvingTechniqueVariant   --> eventuale variante della tecnica risolutiva applicata (null nella maggior parte dei casi)
- changes                   --> la lista di cambiamenti da apportare al sudoku in base a tale deduzione.

--------------------------------------------- Change ---------------------------------------------------------------------

Entità che rappresenta un cambiamento da apportare al Sudoku in seguito alla deduzione di un numero (o una serie di candidati da scartare).
Contiene i segenti campi:
- String solvingTechnique --> la tecnica usata per tale deduzione
- House house             --> la casa (riga, colonna o box) che si è presa in considerazione. Può essere null
- int row                 --> il numero della riga dove andrà apportato il cambiamento
- int col                 --> il numero della colonna dove andrà apportato il cambiamento
- int number              --> il numero da settare con le coordinate di riga e colonna

La classe Change viene estesa dalla sottoclasse Skimming (scrematura).
Tale classe non rappresenta un nuovo numero dedotta da inserire nel Sudoku, bensì dei candidati scartati dai tabs di una cella.
Contiene i seguenti campi:
- tab               --> il nuovo tab della cella, con i restanti candidati
- removedCandidates --> la lista di candidati scartati dalla cella

--------------------------------------------- Tab ---------------------------------------------------------------------

Entità che rappresenta la lista di possibili candidati di una singola cella del Sudoku.
Contiene i seguenti campi:
- int box                   --> numero di box a cui appartiene la cella
- int row                   --> numero di riga a cui appartiene la cella
- int col                   --> numero di colonna a cui appartiene la cella
- List<Integers> numbers    --> i numeri che una cella può ancora potenzialmente ospitare

------------------------------------ Modalità di risoluzione ----------------------------------------------------------------

Definiti questi termini, spieghiamo come avviene la risoluzione effettiva del sudoku.
Il suddetto metodo solve prova inizialmente con le tecniche risolutive più elementari (NakedSingle e HiddenSingle).
Se riesce a dedurre qualcosa, inserisce i nuovi numeri, crea un nuovo oggetto Sudoku con l'aggiunta dei nuovi numeri dedotti e invoca
ricorsivamente il metodo solve su di esso finchè tutti i numeri non vengono dedotti.
Quando NakedSingle e HiddenSingle non riescono a produrre risultati, ricorre allora alle altre tecniche base (Pointing e Claiming Candidates, Hidden e Naked Pair, Triple, Quadruple).

Quando si applica una tecnica risolutiva, viene invocato il metodo check(List<Tab> tabs) di essa.
TUTTE le tecniche risolutive si basano sui tabs, ovvero la lista di potenziali candidati di ogni cella ancora non valorizzata del Sudoku.
Il metodo check restituisce un oggetto SkimmingResult (una scrematura) con i seguenti campi:
- tabs      --> lista dei nuovi tabs (con eventuali candidati scartati)
- changelos --> lista dei changelogs prodotti dalla tecnica risolutiva



