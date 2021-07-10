package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) { // currentStage = Evento atual ou Palco atual
		return (Stage) ((Node) event.getSource()).getScene()
				.getWindow();/*
								 * aqui é feito uma bagunça.... tipo, a gente pega o event;getSource(), mas ele
								 * é muito genérico então fazemos um downCast para o tipo "Node" e a partir
								 * desse Node chamamos o getScene().getWindow() que é para pegar tela da cena.
								 * Como esse getWindow é uma SUPER classe do Stage, a gente faz um novo DownCast
								 * para o tipo "Stage"
								 */
	}

	public static Integer tryParseToInt(String str) {
		/*
		 * Esse método é para tratar o que vem láaaaa da TextField do Id, para que ele
		 * converta o texto (String) do TextField para um numero Inteiro. E, caso não
		 * seja possível fazer tal conversão, vai return null
		 */
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}

	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
}
