package app;

import domain.Part;
import domain.Platform;

import java.io.*;
import java.util.List;

public class SVGGenerator {

    public void write(List<Part> partList, List<Platform> platformList) {
        //TODO: why do empty platforms get created?
        try {
            for(Platform platform : platformList) {
                if(!platform.getPartList().isEmpty()) {
                    File svg = new File("src/main/resources/result/platform" + platform.getId() + ".svg");
                    Writer writer = new BufferedWriter(new FileWriter(svg));

                    writer.write("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n");
                    writer.write("<rect width=\"" + (platform.getWidth() * 100) +
                            "\" height=\"" + (platform.getHeight() * 100) + "\" style=\"fill:gray\"/>\n");
                    for(Part part: partList) {
                        if(part.getPlatform() == platform) {
                            writer.write("<rect x=\"" + (part.getCoordinates().getX() * 100) +
                                        "\" y=\"" + (part.getCoordinates().getY() * 100) +
                                        "\" width=\"" + (part.getWidth() * 100) +
                                        "\" height=\"" + (part.getHeight() * 100) + "\" style=\"fill:blue\" />\n");
                        }
                    }
                    writer.write("</svg>");
                    writer.close();
                }
            }
        } catch(IOException e) {
            System.out.println("IOException caused by FileWriter in SVGGenerator.java");
            System.exit(1);
        }





    }
}
