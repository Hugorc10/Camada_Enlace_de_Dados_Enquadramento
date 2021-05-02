package uesb.redes.view;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import uesb.redes.model.LinePositions;
import uesb.redes.util.BitManipulation;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryTrasmission extends Thread {
    
    public BitManipulation bitManipulation;
    
    Line[] binaryLine1;
    Slider speedSld;
    
    
    public BinaryTrasmission(Line[] binaryLine1, Slider speedSld, BitManipulation bitManipulation) {
        this.binaryLine1 = binaryLine1;
        this.speedSld = speedSld;
        this.bitManipulation = bitManipulation;
    }
    
    public void run() {
        
        int z = LinePositions.STREAM_X.getValue();
        
        binaryLine1[0].setStroke(Color.RED);

//        StringBuilder sb = new StringBuilder();
//        String s = "";

//        for (int i = 0; i < bitManipulation.size(); i++) {
//            s = sb.append(bitManipulation.bitsToString(bitManipulation) + " ").toString();
//            s += bitManipulation.bitsToString(bitManipulation);
//        }
        
        for (int i = bitManipulation.size() - 1; i >= 0; i--) {
            int finalI = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    
                    
                    for (int k = binaryLine1.length - 1; k >= 1; k--) {
                        binaryLine1[k].setStartX(binaryLine1[k - 1].getStartX() + LinePositions.CLOCK_LINE_WIDTH.getValue());
                        binaryLine1[k].setEndX(binaryLine1[k - 1].getEndX() + LinePositions.CLOCK_LINE_WIDTH.getValue());
                        binaryLine1[k].setStartY(binaryLine1[k - 1].getStartY());
                        binaryLine1[k].setEndY(binaryLine1[k - 1].getEndY());
                        binaryLine1[k].setStroke(binaryLine1[k - 1].getStroke());
                    } // fim do for
                    
                    if (bitManipulation.get(finalI) == 0) {
                        binaryLine1[0].setStartX(z);
                        binaryLine1[0].setStartY(LinePositions.NEGATIVE_SIGNAL_Y.getValue());
                        binaryLine1[0].setEndX(z - LinePositions.CLOCK_LINE_WIDTH.getValue());
                        binaryLine1[0].setEndY(LinePositions.NEGATIVE_SIGNAL_Y.getValue());
                    } else if (bitManipulation.get(finalI) == 1) {
                        binaryLine1[0].setStartX(z);
                        binaryLine1[0].setStartY(LinePositions.POSITIVE_SIGNAL_Y.getValue());
                        binaryLine1[0].setEndX(z - LinePositions.CLOCK_LINE_WIDTH.getValue());
                        binaryLine1[0].setEndY(LinePositions.POSITIVE_SIGNAL_Y.getValue());
                    } // fim do if/else
                }
            });
            try {
                Thread.sleep(750 - (int) speedSld.getValue());
            } catch (InterruptedException ex) {
                Logger.getLogger(BinaryTrasmission.class.getName()).log(Level.SEVERE, null, ex);
            } // fim do try/catch
        }
    }
}
