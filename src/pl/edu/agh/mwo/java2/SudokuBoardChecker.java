package pl.edu.agh.mwo.java2;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Details(currentRevision = 3)
public class SudokuBoardChecker {

	// skladowe
	Workbook wb;

	// konstruktor
	public SudokuBoardChecker(Workbook workbook) {
		this.wb = workbook;
	}

	// metody
	public boolean verifyBoardStructure(int sheetIndex) {
		// zmienna do sledzenia czy cokolwiek jest zle
		boolean poprawnosc = true;
		Sheet sheet = this.wb.getSheetAt(sheetIndex);
		// LOGIKA:
		// iteracja po wierszach, a wewnatrz iteracja po komorkach
		// 4 warunki, ktore moga zmienic zmienna poprawnosc: nie ma wiersza, nie ma
		// komorki, komorka inna niz pusty lub cyfry, komorka ma wartosc poza <1,9>

		// iteracja po wierszach
		outer: for (int wiersz = 0; wiersz < 9; wiersz++) {
			// przygotowanie row do kontroli
			Row row = sheet.getRow(wiersz);
			// w przypadku bledu sheet nie jest poprawny
			if (row == null) {
				poprawnosc = false;
				break;
			}
			// wewnetrzna iteracja na komorkach
			for (int c = 0; c < 9; c++) {
				Cell cell = row.getCell(c);
				// w przypadku bledu cell'a, nie jest poprawny
				if (cell == null) {
					poprawnosc = false;
					break outer;
				}
				CellType cellType = cell.getCellTypeEnum();
				if (!(cellType.equals(CellType.NUMERIC) || cellType.equals(CellType.BLANK))) {
					poprawnosc = false;
					break outer;
				}
				// w przypadku numeric czy sa z odpowiedniego zakresu
				if (cellType.equals(CellType.NUMERIC)) {
					if (cell.getNumericCellValue() < 1.0 || cell.getNumericCellValue() > 9.0) {
						poprawnosc = false;
						break outer;
					}
				}
			}

		}

		// koncowa wartosc
		return poprawnosc;
	}

	public boolean verifyBoard(int sheetIndex) {

		/*
		 * ALGORYTM: a) unikalne liczby w kazdej kolumnie (0-9) b) unikalne liczby w
		 * kazdym wierszu (0-9) c) unikalne liczby w kazdym kwadracie 3x3, (0-9
		 * kwadratow) - algorytm dla kwadratow, kazdy kwadrat identifikuja wspolrzedne
		 * (x,y) - inkrementacja wiersza (x) od 0 do 3, przy stepie +3 dla wiersza - dla
		 * kazdego wiersza (x) inkrementacja kolumny (y) od 0 do 3 ze stepem +3
		 */

		// 3 zmienne logiczne, na podstawie ktorych bedzie zwracany wynik
		boolean poprawnoscWierszy = true;
		boolean poprawnoscKolumn = true;
		boolean poprawnoscKwadratow = true;

		Sheet sheet = this.wb.getSheetAt(sheetIndex);

		// najpierw pobranie planszy
		Cell[][] listaWierszy = getBoard(sheet);
		poprawnoscWierszy = verifyRows(listaWierszy);

		poprawnoscKolumn = verifyRows(listaWierszy);

		poprawnoscKwadratow = verifySquares(listaWierszy);

		return (poprawnoscWierszy && poprawnoscKolumn && poprawnoscKwadratow);
	}

	public boolean verifyRows(Cell[][] tablicaKomorek) {
		// 1. sprawdzenie poprawnosci wierszy
		// pobieramy z kazdego wiersza wartosc i zapisujemy do ArrayList oraz HasSet
		// na koniec porownujemy wielkosc dwoch collection, jak jest rozny, to
		// poprawnoscWierszy false i break
		boolean poprawnoscWierszy = true;
		for (Cell[] tablica : tablicaKomorek) {
			// warunek na cel dalszego sprawdzania
			if (poprawnoscWierszy == false)
				break;
			ArrayList<Double> lista = new ArrayList<Double>();
			HashSet<Double> zbior = new HashSet<Double>();
			for (Cell c : tablica) {
				CellType cellType = c.getCellTypeEnum();
				if (cellType.equals(CellType.NUMERIC)) {
					double value = c.getNumericCellValue();
					lista.add(value);
					zbior.add(value);
				}
			}
			// porownanie czy sa powtorzenia
			if (lista.size() != zbior.size())
				poprawnoscWierszy = false;
		}
		return poprawnoscWierszy;
	}

	public boolean verifyColumns(Cell[][] tablicaKomorek) {
		// 2. sprawdzenie poprawnosci kolumn
		boolean poprawnoscKolumn = true;
		for (int kolumna = 0; kolumna < 9; kolumna++) {
			// warunek na cel dalszego sprawdzania
			if (poprawnoscKolumn == false)
				break;
			// collections do porownania
			ArrayList<Double> listaKolumn = new ArrayList<Double>();
			HashSet<Double> zbiorKolumn = new HashSet<Double>();
			// dla i kolumn od 0 do 9 kolumn pobierz wszystkie wiersze, pobierz z nich
			// element Cell pod indeksem i i zapisz do List i Set
			for (Cell[] element : tablicaKomorek) {
				Cell c = element[kolumna];
				CellType cellType = c.getCellTypeEnum();
				if (cellType.equals(CellType.NUMERIC)) {
					double value = c.getNumericCellValue();
					listaKolumn.add(value);
					zbiorKolumn.add(value);
				}
				// porownanie czy sa powtorzenia
				if (listaKolumn.size() != zbiorKolumn.size())
					poprawnoscKolumn = false;
			}
		}
		return poprawnoscKolumn;
	}

	public boolean verifySquares(Cell[][] tablicaKomorek) {
		boolean poprawnoscKwadratow = true;
		// 3. sprawdzenie poprawnosci kwadratow
		for (int w = 0; w < 9; w += 3) {
			// warunek na cel dalszego sprawdzania
			if (poprawnoscKwadratow == false)
				break;
			for (int k = 0; k < 9; k += 3) {
				// warunek na cel dalszego sprawdzania
				if (poprawnoscKwadratow == false)
					break;
				// rozpatrujemy tutaj konkretny kwardrat
				ArrayList<Double> listaKwadrat = new ArrayList<Double>();
				HashSet<Double> zbiorKwadrat = new HashSet<Double>();
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						// dla celow testowania
						// String s=String.format("kwadrat: (%d,%d)\t wiersz: %d, kolumna: %d", w, k,
						// w+i, k+j);
						// System.out.println(s);
						Cell c = tablicaKomorek[w + i][k + j];
						CellType cellType = c.getCellTypeEnum();
						if (cellType.equals(CellType.NUMERIC)) {
							double value = c.getNumericCellValue();
							listaKwadrat.add(value);
							zbiorKwadrat.add(value);
						}
					}
				}
				// porownanie czy sa powtorzenia
				if (listaKwadrat.size() != zbiorKwadrat.size()) {
					poprawnoscKwadratow = false;
				}
			}
		}
		return poprawnoscKwadratow;
	}

	public Cell[][] getBoard(Sheet sheet) {
		Cell[][] tablicaKomorek = new Cell[9][9];
		for (int wiersz = 0; wiersz < 9; wiersz++) {
			Row row = sheet.getRow(wiersz);
			for (int komorka = 0; komorka < 9; komorka++) {
				Cell cell = row.getCell(komorka);
				tablicaKomorek[wiersz][komorka] = cell;
			}
		}
		return tablicaKomorek;
	}
}