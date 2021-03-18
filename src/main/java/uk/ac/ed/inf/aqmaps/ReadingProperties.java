package uk.ac.ed.inf.aqmaps;

public class ReadingProperties {
	private String color; // RGB String
	private String symbol; // Lighthouse/ cross may create enum later instead
	private String colorName;
	
	public ReadingProperties(String pollutionLevel) {
		try {
			var pol = Double.parseDouble(pollutionLevel);
			if (0 > pol || pol > 256) { // since no value is in this range, I use it to indicate that sensor with "-1" reading has not been visited
				color = "#aaaaaa";
				colorName = "Gray";
			}
			else if (pol < 32) {
				color =  "#00ff00";
				colorName = "Green";
				symbol = "lighthouse";
			}
			else if (pol < 64) {
				color =  "#40ff00";
				colorName = "Medium Green";
				symbol = "lighthouse";
			}
			else if (pol < 96) {
				color =  "#80ff00";
				colorName = "Light Green";
				symbol = "lighthouse";
			}
			else if (pol < 128) {
				color =  "#c0ff00";
				colorName = "Lime Green";
				symbol = "lighthouse";
			}
			else if (pol < 160) {
				color =  "#ffc000";
				colorName = "Gold";
				symbol = "danger";
			}
			else if (pol < 192) {
				color =  "#ff8000";
				colorName = "Orange";
				symbol = "danger";
			}
			else if (pol < 224) {
				color =  "#ff4000";
				colorName = "Red / Orange ";
				symbol = "danger";
			}
			else if (pol < 256) {
				color =  "#ff0000";
				colorName = "Red";
				symbol = "danger";
			}
		} catch (Exception e) {
//			System.out.println(e);
//			System.out.println(pollutionLevel);
			color =  "#000000";
			colorName = "Black";
			symbol = "cross";
		}	
	}

	public String getSymbol() {
		return symbol;
	}

	public String getRGB() {
		return color;
	}

	public String getColorName() {
		return colorName;
	}
}
