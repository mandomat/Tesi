package it.sp4te.css.graphgenerator;

import static com.googlecode.charts4j.Color.*;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;

/**
 * <p>Titolo: GraphGenerator</p>
 * <p>Descrizione: Classe per la creazione dei grafici </p>
 * @author Pietro Coronas**/

public class GraphGenerator {

	/**
	 * Metodo per la creazione del grafico
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui � stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui � stata effettuata la simulazione
	 * @throws IOException 
	 **/

	public static void drawGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup) throws IOException {
		ArrayList<Color> colorList=generateColor();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
        
		for (String graphName : detection.keySet()) {
			Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(Shape.CIRCLE, colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("% of Detection", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));
		
		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
        chart.setAreaFill(fill);

		// Mostro il grafico tramite java swing
		displayUrlString(chart.toURLString());
	}

	/** 
	 * Metodo per visualizzare il grafico in una finestra Java Swing 
	 * @param urlString Url dell'oggetto chart
	 *  **/
	
	private static void displayUrlString(final String urlString) throws IOException {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(ImageIO.read(new URL(urlString))));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

	}

	/** 
	 * Metodo per la generazione del colore Random.
	 * 
	 *  @return Una lista di 5 colori da utilizzare per le curve. Un grafico con pi� di 5 curve
	 *  diventa difficilmente leggibile
	 *  **/
	
	private static ArrayList<Color> generateColor() {
		ArrayList<Color> colorList= new ArrayList<Color>();
		colorList.add(Color.BLUE);
		colorList.add(Color.RED);
		colorList.add(Color.GREEN);
		colorList.add(Color.VIOLET);
		colorList.add(Color.ORANGE);
		
		
			return colorList;
	}

}