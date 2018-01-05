package pl.edu.agh.mwo.java2;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Details(currentRevision=2)
public class App {

	public static void main(String[] args) {
		/* 
		 * 1. otworzyc worksheet 
		 * 2. sprawdzic na kazdym sheet, czy plansza jest ok strukturalnie
		 * 3. sprawdzic czy pola sa dobrze wypelnione (wg logiki sudoku)
		 * */

		// otwarcie sudoku.xlsx
		try {
			Workbook wb = WorkbookFactory.create(new File("sudoku.xlsx"));
			SudokuBoardChecker sbc=new SudokuBoardChecker(wb);
			// powinno sie pobrac liczbe sheet'ow, ale dla uproszczenia odczytuje
			short sheetNumbers=7;
			for (int sn=0;sn<sheetNumbers;sn++) {
				boolean poprawnosc;
				//CWICZENIE 1: Sprawdzanie poprawnosci kart
				poprawnosc=sbc.verifyBoardStructure(sn);
				//CWICZENIE 2: Sprawdzanie poprawnosci danych
				boolean poprawnosc2=sbc.verifyBoard(sn);
				// sheet number +1 dla czytelnosci (sa od zera, ale nazwane sa od 1)
				String s1=String.format("METODA 1, sheet %d : poprawnosc syntaktyczna: %7b\tpoprawnosc danych: %7b\t ogolna poprawnosc: %7b", sn+1, poprawnosc, poprawnosc2, poprawnosc&poprawnosc2);
				System.out.println(s1);
		
				//CWICZENIE 2: Sprawdzanie poprawnosci danych
				boolean poprawnosc2b=sbc.verifyBoard2(sn);
				// sheet number +1 dla czytelnosci (sa od zera, ale nazwane sa od 1)
				String s2=String.format("METODA 2, sheet %d : poprawnosc syntaktyczna: %7b\tpoprawnosc danych: %7b\t ogolna poprawnosc: %7b", sn+1, poprawnosc, poprawnosc2b, poprawnosc&poprawnosc2b);
				System.out.println(s2);
			}

		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				e.printStackTrace();
		}
	}
}
