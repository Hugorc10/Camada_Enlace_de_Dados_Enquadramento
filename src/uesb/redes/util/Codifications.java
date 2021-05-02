package uesb.redes.util;

import javafx.scene.control.Alert;
import uesb.redes.view.BinaryTrasmission;
import uesb.redes.view.DifManchesterTransmission;
import uesb.redes.view.ManchesterTransmission;
import uesb.redes.view.ScreenView;

public class Codifications {
    
    private static BinaryTrasmission bt;
    private static ManchesterTransmission mt;
    private static DifManchesterTransmission dmt;
    // tipo de codificacao
    public static int encodingType;
    // tipo de enquadramento
    public static int framingType;
    
    /**
     * Metodo: transmittingApplication
     * Funcao: armazenar o texto digitado em uma string e enviar para a proxima camada
     */
    public static void transmittingApplication() {
        // armazena a texto digitado na String message
        String message = ScreenView.inputTxt.getText();
        
        // chama a proxima camada
        transmittingAplicationLayer(message);
    } // fim do metodo transmittingApplication
    
    
    /**
     * Metodo: transmittingAplicationLayer
     * Funcao: recebe a mensagem passada como argumento e converte para um conjunto de bits
     *
     * @param message
     */
    private static void transmittingAplicationLayer(String message) {
        System.out.println("Codifications.transmittingAplicationLayer");
        
        BitManipulation packets = new BitManipulation();
        packets.stringToBits(message);
        
        if (ScreenView.inputTxt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("The input box is empty!");
            alert.setTitle("Alert");
            alert.showAndWait();
        } else if (ScreenView.group.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("The radio buttons are not select!");
            alert.setTitle("Alert");
            alert.showAndWait();
        } else if (!(ScreenView.characterStufingChk.isSelected() || ScreenView.framingChk.isSelected() ||
                ScreenView.bitStufingChk.isSelected() || ScreenView.violacaoChk.isSelected())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("The check marks are not select!");
            alert.setTitle("Alert");
            alert.showAndWait();
        }
        
        System.out.println("-----Codifications.transmittingAplicationLayer-----");
        transmittingFramingDataLinkLayer(packets);
    } // fim do metodo transmittingAplicationLayer
    
    /**
     * Metodo: transmittingFramingDataLinkLayer
     * Funcao: escolher o tipo de enquadramento e enviar para proxima camada
     *
     * @param packets
     */
    private static void transmittingFramingDataLinkLayer(BitManipulation packets) {
        System.out.println("Codifications.transmittingFramingDataLinkLayer");
        
        switch (framingType) {
            case 1: // contagem de caracteres
                encodingByteCount(packets);
                break;
            case 2: // insercao de bytes
                encodingByteStuffing(packets);
                break;
            case 3: // insercao de bits
                encodingBitStuffing(packets);
                break;
            case 4: // violacao da camada fisica
//                encodingPhysicalLayerCodingViolations(packets);
                break;
        } // fim do switch/case
        System.out.println("-----Codifications.transmittingFramingDataLinkLayer-----");
    } // fim do metodo transmittingFramingDataLinkLayer
    
    
    /**
     * Metodo: encodingByteCount
     * Funcao:
     *
     * @param packets
     */
    private static void encodingByteCount(BitManipulation packets) {
        System.out.println("Codifications.encodingByteCount");
        
        // numero de caracteres presentes no quadro
        int charactersCount = (packets.size() / 8);
        // numero de caracteres inseridos no quadroEnquadrado
        int insertedCharactersNumber = 0;
        // tamanho do quadro
        int frameSize = 6;
        // numero de quadros
        int frameCount = (charactersCount / (frameSize - 1));
        
        if (charactersCount % (frameSize - 1) != 0) {
            frameCount++;
        }
        
        String sizeCharacterFrame = ""; // caractere equivalente ao tamanho do quadro
        
        for (int i = 0; i < frameCount; i++) {
            // bits equivalente ao tamanho do quadro
            BitManipulation frameBitsSize = new BitManipulation();
            // quadro enquadrado
            BitManipulation delimitedFrame = new BitManipulation();
            
            if (i == (frameCount - 1) && charactersCount < (frameSize - 1)) {
                // tamanho do quadro fica menor caso o numero de caracteres seja menor que 5
                frameSize = charactersCount + 1;
            }
            
            // converte o valor do 'frameSize' para string
            sizeCharacterFrame = "" + frameSize;
            // converte o valor do 'frameSize' da String para bits
            frameBitsSize.stringToBits(sizeCharacterFrame);
            
            // 'delimitedFrame' recebe o byte equivalente ao size do quadro
            for (int j = 0; j < 8; j++) {
                if (frameBitsSize.get(j) == 1) {
                    delimitedFrame.addBit(1); //adiciona o bit 1
                } else {
                    delimitedFrame.addBit(0); //adiciona o bit 0
                }
            } // fim do for
            
            // 'delimitedFrame' recebe os valores do quadro
            for (int j = 0; j < frameSize - 1; j++) {
                // 'delimitedFrame' recebe o caractere
                for (int k = 0; k < 8; k++) {
                    if (packets.get(insertedCharactersNumber * 8 + k) == 1) {
                        // adiciona o bit 1
                        delimitedFrame.addBit(1);
                    } else {
                        // adiciona o bit 0
                        delimitedFrame.addBit(0);
                    }
                } // fim do for
                
                // insere um caractere no quadroEnquadrado
                insertedCharactersNumber++;
                
                // retira um caractere do quadro
                charactersCount--;
            } //fim do for
            
            System.out.println("-----Codifications.encodingByteCount-----");
            // chama a proxima camada
            transmittingPhysicalLayer(delimitedFrame);
        } // fim do for
    } // fim do metodo encodingByteCount
    
    /**
     * Metodo: encodingByteStuffing
     * Funcao:
     *
     * @param packets
     */
    static void encodingByteStuffing(BitManipulation packets) {
        System.out.println("-----Codifications.encodingByteStuffing-----");
        
        // caracter escolhido como FLAG
        char flagChar = '#';
        // caracter escolhido com ESC
        char escChar = '!';
        int caractereSize = (packets.size() / 8);
        // numero de caracteres presentes no quadro
        int insertedCharactersNumber = 0;
        // numero de caracteres inseridos no quadroEnquadrado
        int frameSize = 5;
        // conta o numero de quadros
        int framesCount = (caractereSize / (frameSize));
        
        if (caractereSize % (frameSize) != 0) {
            framesCount++;
        }
        
        // String equivalente a FLAG
        String flagString;
        // String equivalente ao ESC
        String escString;
        // bits equivalente a flag
        BitManipulation flagBits = new BitManipulation();
        // bits equivalente ao esc
        BitManipulation escBits = new BitManipulation();
        
        // converte o caractere flag para String
        flagString = "" + flagChar;
        // converte o caractere esc para String
        escString = "" + escChar;
        // converte flagString para o conjunto de bits
        flagBits.stringToBits(flagString);
        // converte escString para o conjunto de bits
        escBits.stringToBits(escString);
        
        for (int i = 0; i < framesCount; i++) {
            
            // quadro enquadrado
            BitManipulation delimitedFrame = new BitManipulation();
            
            if (i == (framesCount - 1) && caractereSize < frameSize) {
                frameSize = caractereSize; //size de quadro fica menor caso o numero de Caracteres seja < 5
            }
            
            //quadroEnquadrado recebe o byte equivalente ao flag
            for (int j = 0; j < 8; j++) {
                if (flagBits.get(j) == 1)
                    delimitedFrame.addBit(1); //adiciona o bit 1
                else
                    delimitedFrame.addBit(0); //adiciona o bit 0
            } // fim do for
            
            for (int j = 0; j < frameSize; j++) {
                BitManipulation aux = new BitManipulation(); // bits auxiliar
                
                // aux recebe o caracteres
                for (int k = 0; k < 8; k++) {
                    if (packets.get(insertedCharactersNumber * 8 + k) == 1) {
                        // adiciona um bit 1
                        aux.addBit(1);
                    } else {
                        // adiciona um bit 0
                        aux.addBit(0);
                    }
                } // fim do for
                
                String charValue;
                charValue = aux.bitsToString(aux);
                
                if (charValue.equals(flagString) || charValue.equals(escString)) {
                    // coloca um ESC
                    // 'delimitedFrame' recebe o byte equivalente ao esc
                    for (int k = 0; k < 8; k++) {
                        if (escBits.get(k) == 1)
                            delimitedFrame.addBit(1); //adiciona o bit 1
                        else
                            delimitedFrame.addBit(0); //adiciona o bit 0
                    } // fim do for
                    
                } // fim do ir
                
                // 'delimitedFrame' recebe o caractere
                for (int k = 0; k < 8; k++) {
                    if (packets.get(insertedCharactersNumber * 8 + k) == 1)
                        delimitedFrame.addBit(1); //adiciona o bit 1
                    else
                        delimitedFrame.addBit(0); //adiciona o bit 0
                } // fim do for
                
                // insere um caractere no quadroEnquadrado
                insertedCharactersNumber++;
                // retira um caractere do quadro
                caractereSize--;
            } // fim do for
            
            // 'delimitedFrame' recebe o byte equivalente ao flag
            for (int j = 0; j < 8; j++) {
                if (flagBits.get(j) == 1)
                    delimitedFrame.addBit(1); // adiciona o bit 1
                else
                    delimitedFrame.addBit(0); // adiciona o bit 0
            } // fim do for
            
            System.out.println("-----Codifications.encodingByteStuffing-----");
            // chama proxima camada
            transmittingPhysicalLayer(delimitedFrame);
            
        } // fim do for
    } // fim do metodo encodingByteStuffing
    
    /**
     * Metodo: encodingBitStuffing
     * Funcao:
     *
     * @param packets
     */
    private static void encodingBitStuffing(BitManipulation packets) {
        System.out.println("Codifications.encodingBitStuffing");
        
        char flagChar = '~'; // 01111110
        int bitsSequence = 0; //sequencia de Bits
        int sentBis = 0; //numero de bits enviados
        int numeroBitsFaltando = packets.size(); //numero de bits a serem transmitidos
        int numeroCaracteres = (packets.size() / 8);
        int numeroQuadrosEnviados = 0; //numero de quadros enviados
        int frameSize = 5;
        int numeroQuadros = (numeroCaracteres / (frameSize));
        if (numeroCaracteres % (frameSize) != 0) {
            numeroQuadros++;
        }
        
        // string equivalente ao FLAG
        String flagString = "";
        BitManipulation flagBits = new BitManipulation(); // bits equivalente ao FLAG
        
        flagString = "" + flagChar; // converte o caractere FLAG para String
        flagBits.stringToBits(flagString); // converte flagString para um conjunto de bits
        
        for (int i = 0; i < numeroQuadros; i++) {
            
            bitsSequence = 0;
            // quadro enquadrado
            BitManipulation delimitedFrame = new BitManipulation();
            if (i == (numeroQuadros - 1) && numeroCaracteres < frameSize) {
                // tamanho do quadro fica menor caso o numero de caracteres seja menor que 5
                frameSize = numeroCaracteres;
            } // fim do if
            
            // 'delimitedFrame' recebe os bits da flag
            for (int j = 0; j < 8; j++) {
                if (flagBits.get(j) == 1) {
                    delimitedFrame.addBit(1); //adiciona o bit 1
                } else {
                    delimitedFrame.addBit(0); //adiciona o bit 0
                } // fim do else
            }//fim for
            
            // quadroEnquadrado recebe os valores do quadro
            for (int j = 0; j < (frameSize * 8); j++) {
                if (bitsSequence == 5) {
                    delimitedFrame.addBit(0);//adciona o bit 0
                    bitsSequence = 0;
                } //fim do if
                
                if (packets.get(sentBis) == 1) {
                    bitsSequence++;
                    delimitedFrame.addBit(1); //adiciona o bit 1
                } else {
                    bitsSequence = 0; //zera o contador da sequencia de bits 1
                    delimitedFrame.addBit(0); //adiciona o bit 0
                } //fim do else
                
                sentBis++;
                
                if (sentBis % 8 == 0) {
                    numeroCaracteres--;
                }
            } //fim do for
            
            // 'delimitedFrame' recebe o byte equivalente ao flag
            for (int j = 0; j < 8; j++) {
                if (flagBits.get(j) == 1) {
                    // adiciona um bit 1
                    delimitedFrame.addBit(1);
                } else {
                    // adiciona um bit 0
                    delimitedFrame.addBit(0);
                }
            } // fim do for
            
            // chama proxima camada
            System.out.println("-----Codifications.encodingBitStuffing-----");
            transmittingPhysicalLayer(delimitedFrame);
        } // fim do for
    } // fim do metodo encodingBitStuffing
    
    /**
     * Metodo: encodingPhysicalLayerCodingViolations
     * Funcao:
     *
     * @param packets
     */
    private static void encodingPhysicalLayerCodingViolations(BitManipulation packets) {
        System.out.println("Codifications.encodingPhysicalLayerCodingViolations");
        
        transmittingPhysicalLayer(packets);
    }
    
    /**
     * Metodo: transmittingPhysicalLayer
     * Funcao: verificar o tipo de codificacao e mandar o array codificado para a proxima camada
     *
     * @param frames
     */
    private static void transmittingPhysicalLayer(BitManipulation frames) {
        System.out.println("Codifications.transmittingPhysicalLayer");
        BitManipulation rawBits = null;
        
        if (ScreenView.binaryRad.isSelected()) {
            encodingType = 1;
        } else if (ScreenView.manchesterRad.isSelected()) {
            encodingType = 2;
        } else if (ScreenView.difManchesterRad.isSelected()) {
            encodingType = 3;
        }
        
        switch (encodingType) {
            case 1:
                rawBits = transmittingPhysicalLayerBinaryEncoding(frames);
                break;
            case 2:
                rawBits = transmittingPhysicalLayerManchesterEncoding(frames);
                break;
            case 3:
                rawBits = transmittingPhysicalLayerDiferentialManchesterEncoding(frames);
                break;
        }
        
        System.out.println("-----Codifications.transmittingPhysicalLayer-----");
        communicationChanel(rawBits);
    }
    
    /**
     * Metodo: transmittingPhysicalLayerBinaryEncoding
     * Funcao: simplismente retorna o array de bits
     *
     * @param frames
     * @return BitManipulation
     */
    private static BitManipulation transmittingPhysicalLayerBinaryEncoding(BitManipulation frames) {
        System.out.println("----------Codifications.transmittingPhysicalLayerBinaryEncoding-----------");
        
        System.out.println("Codifications.transmittingPhysicalLayerBinaryEncoding");
        return frames;
    } // fim do metodo transmittingPhysicalLayerBinaryEncoding
    
    /**
     * Metodo: transmittingPhysicalLayerManchesterEncoding
     * Funcao: codificar o array de bits para a codificacao manchester
     *
     * @param frames
     * @return BitManipulation
     */
    private static BitManipulation transmittingPhysicalLayerManchesterEncoding(BitManipulation frames) {
        System.out.println("Codifications.transmittingPhysicalLayerManchesterEncoding");
        
        BitManipulation codification = new BitManipulation();
        
        int size = frames.size();
        
        for (int i = 0; i < size; i++) {
            if (frames.get(i) == 1) {
                codification.addBit(1);
                codification.addBit(0);
            } else {
                codification.addBit(0);
                codification.addBit(1);
            }
        }
        
        System.out.println("-----Codifications.transmittingPhysicalLayerManchesterEncoding-----");
        return codification;
    } // fim do metodo transmittingPhysicalLayerManchesterEncoding
    
    /**
     * Metodo: transmittingPhysicalLayerDiferentialManchesterEncoding
     * Funcao: codificar o array de bits para a codificacao manchester diferencial
     *
     * @param frames
     * @return BitManipulation
     */
    public static BitManipulation transmittingPhysicalLayerDiferentialManchesterEncoding(BitManipulation frames) {
        System.out.println("Codifications.transmittingPhysicalLayerDiferentialManchesterEncoding");
        
        int size = frames.size();
        BitManipulation codification = new BitManipulation();
        
        if (frames.get(0) == 1) {
            codification.addBit(1);
            codification.addBit(0);
        } else {
            codification.addBit(0);
            codification.addBit(1);
        }
        
        for (int i = 1; i < size; i++) {
            if (frames.get(i) == 1) {
                if (codification.get(i * 2 - 1) == 1) {
                    codification.addBit(1);
                    codification.addBit(0);
                } else {
                    codification.addBit(0);
                    codification.addBit(1);
                }
            } else {
                if (codification.get(i * 2 - 1) == 1) {
                    codification.addBit(0);
                    codification.addBit(1);
                } else {
                    codification.addBit(1);
                    codification.addBit(0);
                }
            }
        }
        
        System.out.println("-----Codifications.transmittingPhysicalLayerDiferentialManchesterEncoding-----");
        return codification;
    } // Fim do metodo transmittingPhysicalLayerDiferentialManchesterEncoding
    
    /**
     * Metodo: communicationChanel
     * Funcao: iniciar as Threads e simular uma transmissao de bits brutos com uma animacao em JavaFx
     *
     * @param rawBitStream array de bits
     */
    public static void communicationChanel(BitManipulation rawBitStream) {
        System.out.println("Codifications.communicationChanel");

//        int[] fluxoBrutoDeBitsTransmissor = new int[fluxoBrutoDeBits.length];
//
//        for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
//            fluxoBrutoDeBitsTransmissor[i] = fluxoBrutoDeBits[i];
//        }
//
//        int[] fluxoBrutoDeBitsReceptor = new int[fluxoBrutoDeBits.length];
//
//        for (int i = 0; i < fluxoBrutoDeBitsTransmissor.length; i++) {
//            // fluxoBrutoDeBitsPontoB[i] = (fluxoBrutoDeBitsPontoA[i] and 0000 0000) or (fluxoBrutoDeBitsPontoA[i] and 1111 1111)
//            fluxoBrutoDeBitsReceptor[i] = ((fluxoBrutoDeBitsTransmissor[i] & ~0xff) | (fluxoBrutoDeBitsTransmissor[i] & 0xff));
//            // fluxoBrutoDeBitsPontoB[i] = (fluxoBrutoDeBitsPontoA[i] & 0xfffffff0) | (fluxoBrutoDeBitsPontoA[i] & 0xf);
//        }
//
//        System.out.println("Fluxo de bits receptor: " + Arrays.toString(fluxoBrutoDeBitsReceptor));
        
        switch (encodingType) {
            case 1:
                bt = new BinaryTrasmission(ScreenView.binaryLine1, ScreenView.speedSld, rawBitStream);
                
                mt = new ManchesterTransmission(ScreenView.manchesterLine1, ScreenView.manchesterLine2,
                        ScreenView.manchesterLine3, ScreenView.speedSld, rawBitStream);
                
                dmt = new DifManchesterTransmission(ScreenView.difManchesterLine1, ScreenView.difManchesterLine2,
                        ScreenView.difManchesterLine3, ScreenView.speedSld, rawBitStream);
                
                if (mt.isAlive() || dmt.isAlive()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Wait for the animation to finish");
                    alert.setTitle("Alert");
                    alert.showAndWait();
                } else {
                    bt.setDaemon(true);
                    bt.start();
                }
                
                break;
            case 2:
                bt = new BinaryTrasmission(ScreenView.binaryLine1, ScreenView.speedSld, rawBitStream);
                
                mt = new ManchesterTransmission(ScreenView.manchesterLine1, ScreenView.manchesterLine2,
                        ScreenView.manchesterLine3, ScreenView.speedSld, rawBitStream);
                
                dmt = new DifManchesterTransmission(ScreenView.difManchesterLine1, ScreenView.difManchesterLine2,
                        ScreenView.difManchesterLine3, ScreenView.speedSld, rawBitStream);
                
                if (bt.isAlive() || dmt.isAlive()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Wait for the animation to finish");
                    alert.setTitle("Alert");
                    alert.showAndWait();
                } else {
                    mt.setDaemon(true);
                    mt.start();
                }
                
                break;
            case 3:
//                bt = new BinaryTrasmission(ScreenView.binaryLine1, ScreenView.speedSld, rawBitStream);

//                mt = new ManchesterTransmission(ScreenView.manchesterLine1, ScreenView.manchesterLine2,
//                        ScreenView.manchesterLine3, ScreenView.speedSld, rawBitStream);
                
                dmt = new DifManchesterTransmission(ScreenView.difManchesterLine1, ScreenView.difManchesterLine2,
                        ScreenView.difManchesterLine3, ScreenView.speedSld, rawBitStream);
                
                if (bt.isAlive() || mt.isAlive()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Wait for the animation to finish");
                    alert.setTitle("Alert");
                    alert.showAndWait();
                } else {
                    dmt.setDaemon(true);
                    dmt.start();
                }
                
                break;
        }
        
        System.out.println("-----Codifications.communicationChanel-----");
        receivingPhysicalLayer(rawBitStream);
    } // fim do metodo communicationChanel
    
    /**
     * Metodo: receivingPhysicalLayer
     * Funcao: receber um array de bits, converte para String e imprimi a String de bits
     *
     * @param rawBitStream
     * @return void
     */
    private static void receivingPhysicalLayer(BitManipulation rawBitStream) {
        System.out.println("Codifications.receivingPhysicalLayer");
        BitManipulation rawBits = new BitManipulation();
        
        switch (encodingType) {
            case 1:
                // string que ira receber os bits
                String s = "";
                
                for (int i = 0; i < rawBitStream.size(); i++) {
                    s += String.valueOf(rawBitStream.get(i));
                }
                
                ScreenView.bitsTArea.setText(s);
                
                rawBits = receivingBinaryDecoding(rawBitStream);
                
                break;
            case 2:
                // string que ira receber os bits
                String z = "";
                
                for (int i = 0; i < rawBitStream.size(); i++) {
                    z += String.valueOf(rawBitStream.get(i));
                }
                
                ScreenView.bitsTArea.setText(z);
                
                rawBits = receivingManchesterDecoding(rawBitStream);
                break;
            case 3:
                // string que ira receber os bits
                String k = "";
                
                for (int i = 0; i < rawBitStream.size(); i++) {
                    k += rawBitStream.get(i);
                }
                
                ScreenView.bitsTArea.setText(k);
                
                rawBits = receivingDifferentialManchesterDecoding(rawBitStream);
                break;
        }
        
        System.out.println("-----Codifications.receivingPhysicalLayer-----");
        receivingDataLinkLayer(rawBits); // Chama a proxima camada
    } // Fim do metodo receivingPhysicalLayer
    
    /**
     * Metodo: receivingBinaryDecoding
     * Funcao: receber e retornar o fluxo bruto de bits
     *
     * @param rawBitStream
     * @return int[]
     */
    private static BitManipulation receivingBinaryDecoding(BitManipulation rawBitStream) {
        System.out.println("-----Codifications.receivingBinaryDecoding-----");
        
        if (ScreenView.violacaoMeioFisico) {
            return null;
        } else {
            // retorna os bits
            return rawBitStream;
        } // fim else
    } // Fim do metodo receivingBinaryDecoding
    
    /**
     * Metodo: camadaFisicaReceptoraDecoficacaoManchester
     * Funcao: receber o fluxo bruto de bits codificado em Manchester e decodifica-lo
     *
     * @param rawBitsStream
     * @return String
     */
    public static BitManipulation receivingManchesterDecoding(BitManipulation rawBitsStream) {
        System.out.println("Codifications.receivingManchesterDecoding");
        
        BitManipulation aux = new BitManipulation(); // bits de bits auxiliar
        int pos = 0;
        int size = rawBitsStream.size();
        
        for (int i = pos; i < size; i = i + 2) {
            if (rawBitsStream.get(i) == 1 && rawBitsStream.get(i + 1) == 0) {
                aux.addBit(1);
            } else {
                aux.addBit(0);
            } // fim do else
            
        } // fim do for
        
        System.out.println("-----Codifications.receivingManchesterDecoding-----");
        return aux; // retona os bits
    } // fim do metodo receivingManchesterDecoding
    
    /**
     * Metodo: receivingDifferentialManchesterDecoding
     * Funcao:
     *
     * @param rawBitStream
     */
    private static BitManipulation receivingDifferentialManchesterDecoding(BitManipulation rawBitStream) {
        System.out.println("Codifications.receivingDifferentialManchesterDecoding");
        
        BitManipulation aux = new BitManipulation(); // bits de bits auxiliar
        int pos = 0;
        int size = rawBitStream.size();
        
        if (rawBitStream.get(pos) == 1) {
            aux.addBit(1); // define como 1 o bit da posicao
        }// fim do if
        else {
            aux.addBit(0); // define como 0 o bit da posicao
        }// fim do else
        
        pos += 2;
        
        for (int i = pos; i < size; i = i + 2) {
            
            if (rawBitStream.get(i) == 1) {
                if (rawBitStream.get(i - 2) == 1) {
                    aux.addBit(0);
                } else {
                    aux.addBit(1);
                } // fim do else
            } else {
                if (rawBitStream.get(i - 2) == 1) {
                    aux.addBit(1);
                } else {
                    aux.addBit(0);
                } // fim do else
                
            } //fim do else
        } // fim do for
        
        System.out.println("-----Codifications.receivingDifferentialManchesterDecoding-----");
        return aux;
        
    }
    
    /**
     * Metodo: receivingDataLinkLayer
     * Funcao:
     *
     * @param rawBitStream
     */
    public static void receivingDataLinkLayer(BitManipulation rawBitStream) {
        System.out.println("Codifications.receivingDataLinkLayer");
        
        switch (framingType) {
            case 1: // contagem de caracteres
                decodingByteCount(rawBitStream);
                break;
            case 2: // insercao de bytes
                decodingByteStuffing(rawBitStream);
                break;
            case 3: // insercao de bits
                decodingBitStuffing(rawBitStream);
                break;
            case 4: // violacao da camada fisica
//                decodingPhysicalLayerCodingViolations(rawBitStream);
                break;
        } // fim do switch/case
        
        System.out.println("-----Codifications.receivingDataLinkLayer-----");
    } // fim do metodo receivingDataLinkLayer
    
    private static void decodingByteCount(BitManipulation rawBitStream) {
        System.out.println("Codifications.decodingByteCount");
        
        int numeroCaracteresInseridos = 1; //numero de caracteres inseridos no quadroDesenquadrado
        int frameSize = 0;
        // caractere equivalente ao tamanho do quadro
        String charactereFrameSize;
        BitManipulation tamanhoQuadroBits = new BitManipulation(); //bits equivalente ao size do quadro
        BitManipulation quadroDesenquadrado = new BitManipulation(); //bits equivalente ao quadro Desenquadrado
        
        // tamanho do quadro a ser lido
        for (int i = 0; i < 8; i++) {
            if (rawBitStream.get(i) == 1)
                tamanhoQuadroBits.addBit(1); //adiciona o bit 1
            else
                tamanhoQuadroBits.addBit(0); //adiciona o bit 0
        } //fim do for
        
        // converte de bits para String
        charactereFrameSize = tamanhoQuadroBits.bitsToString(tamanhoQuadroBits);
        
        // converte String para inteiro
        frameSize = Integer.parseInt(charactereFrameSize);
        
        // quadroDesenquadrado recebe os valores do quadro
        for (int j = 0; j < frameSize - 1; j++) {
            
            // quadroEnquadrado recebe o caractere
            for (int k = 0; k < 8; k++) {
                if (rawBitStream.get(numeroCaracteresInseridos * 8 + k) == 1)
                    quadroDesenquadrado.addBit(1); //adiciona o bit 1
                else
                    quadroDesenquadrado.addBit(0); //adiciona o bit 0
            }//fim for
            
            numeroCaracteresInseridos++; // inseriu um caractere no quadroDesenquadrado
        } // fim do for
        
        System.out.println("-----Codifications.decodingByteCount-----");
        // chama a proxima camada
        receivingApplicationLayer(quadroDesenquadrado);
    }//fim do metodo CamadaDeEnlaceDadosReceptoraContagemDeCaracteres
    
    private static void decodingByteStuffing(BitManipulation rawBitStream) {
        System.out.println("Codifications.decodingByteStuffing");
        
        char flagChar = '#';
        char escChar = '!';
        int numeroCaracteresInseridos = 1;
        String flagString = ""; // String equivalente a FLAG
        String escString = ""; // String equivalente ao ESC
        String flagAtual; // String equivalente ao possivel FLAG do quadro
        String escAtual; // String equivalente ao possivel ESC do quadro
        BitManipulation flagBits = new BitManipulation(); //bits equivalente a flag
        BitManipulation escBits = new BitManipulation(); //bits equivalente ao esc
        BitManipulation quadroDesenquadrado = new BitManipulation(); //quadro Desenquadrado
        BitManipulation atual = new BitManipulation(); //bits do byte atual
        boolean fim = false;
        
        flagString += flagChar; //converte o caractere flag para String
        escString += escChar; //converte o caractere esc para String
        flagBits.stringToBits(flagString); //converte flagString para o conjunto de bits
        escBits.stringToBits(escString); //converte escString para o conjunto de bits
        
        //atual recebe o byte equivalente ao comeco do quadro
        for (int i = 0; i < 8; i++) {
            if (rawBitStream.get(i) == 1)
                atual.addBit(1); //adiciona o bit 1
            else
                atual.addBit(0); //adiciona o bit 0
        }//fim for
        
        flagAtual = atual.bitsToString(atual);
        
        //Verifica se o recebeu a flag para iniciar a leitura do quadro
        if (flagAtual.equals(flagString)) {
            
            //quadroDesenquadrado recebe os valores do quadro
            while (fim == false) {
                
                BitManipulation aux = new BitManipulation(); //bits auxiliar
                
                // aux recebe o prox byte
                for (int k = 0; k < 8; k++) {
                    if (rawBitStream.get(numeroCaracteresInseridos * 8 + k) == 1)
                        aux.addBit(1); //adiciona o bit 1
                    else
                        aux.addBit(0); //adiciona o bit 0
                } // fim for
                
                String valorChar;
                valorChar = aux.bitsToString(aux);
                
                if (valorChar.equals(escString)) {
                    // retira o ESC
                    numeroCaracteresInseridos++;
                }//fim if
                if (valorChar.equals(flagString)) {
                    // chegou ao fim
                    fim = true;
                    break;
                }//fim if
                
                // quadroEnquadrado recebe o caractere
                for (int k = 0; k < 8; k++) {
                    if (rawBitStream.get(numeroCaracteresInseridos * 8 + k) == 1)
                        quadroDesenquadrado.addBit(1); //adiciona o bit 1
                    else
                        quadroDesenquadrado.addBit(0); //adiciona o bit 0
                }//fim for
                
                numeroCaracteresInseridos++; //inseriu um caractere no quadroEnquadrado
                
            }//fim while
            
            System.out.println("-----Codifications.decodingByteStuffing-----");
            // chama a proxima camada
            receivingApplicationLayer(quadroDesenquadrado);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error!");
            alert.setTitle("Error");
            alert.showAndWait();
        }
    } // fim do metodo decodingByteStuffing
    
    private static void decodingBitStuffing(BitManipulation rawBitStream) {
        System.out.println("Codifications.decodingBitStuffing");
        
        boolean end = false;
        boolean sequence = false; //caso encontre uma sequencia de 5 bits 1
        char flagChar = '~';
        int numeroBitsInseridos = 8; //numero de bits inseridos no quadroEnquadrado
        int bitsCount = 0; //numero de bits 1 consecutivos recebidos
        String flagString = ""; // string equivalente a FLAG
        BitManipulation flagBits = new BitManipulation(); //bits equivalente a flag
        BitManipulation current = new BitManipulation(); //bits para receber a flag inicio
        BitManipulation dataFrame = new BitManipulation(); //quadro enquadrado
        
        flagString += flagChar; //converte o caractere flag para String
        flagBits.stringToBits(flagString); //converte flagString para o conjunto de bits
        
        //atual recebe o byte equivalente ao comeco do quadro
        for (int i = 0; i < 8; i++) {
            if (rawBitStream.get(i) == 1) {
                current.addBit(1); // adiciona um bit 1
            } else {
                current.addBit(0); // adiciona um bit 0
            }
        } // fim do for
        
        String flagAtual = current.bitsToString(current);
        
        // verifica se o recebeu a flag para iniciar a leitura do quadro
        if (flagAtual.equals(flagString)) {
            //quadroEnquadrado recebe os valores do quadro
            while (end == false) {
                
                // aux recebe o os bits
                if (rawBitStream.get(numeroBitsInseridos) == 1) {
                    bitsCount++;
                    if (bitsCount == 6) {
                        for (int l = 0; l < 6; l++) {
                            dataFrame.removeBit(); //retira o bit
                        }
                        end = true;
                        //deleta os 5 bits
                        break;
                    } else {
                        dataFrame.addBit(1); // adiciona um bit 1
                    }
                } else {
                    if (bitsCount != 5) {
                        dataFrame.addBit(0); //adiciona um bit 0
                    } //fim do else
                    bitsCount = 0; //zero o contadorDeBits
                } //fim do else
                
                numeroBitsInseridos++;
            } // fim do while
            
            System.out.println("-----Codifications.decodingBitStuffing-----");
            receivingApplicationLayer(dataFrame);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error!");
            alert.setTitle("Error");
            alert.showAndWait();
        } // fim do else
    }
    
    private static void decodingPhysicalLayerCodingViolations(BitManipulation rawBitStream) {
        receivingApplicationLayer(rawBitStream);
    }
    
    /**
     * Metodo: receivingApplicationLayer
     * Funcao: converte os bits que estao no TextArea para char e depois mostra a mensagem TextField receptor
     *
     * @param packets array de bits
     */
    private static void receivingApplicationLayer(BitManipulation packets) {
        System.out.println("Codifications.receivingApplicationLayer");
        
        // String que recebe o conjunto de bits em forma de texto
        String s = ScreenView.bitsTArea.getText();
        
        System.out.println("Imprimindo s: " + s);
        String str = "";
        if (framingType == 1 && encodingType == 1) {
            
            for (int i = 0; i < s.length() / 8; i++) {
                int a = Integer.parseInt(s.substring(8 * i, (i + 1) * 8), 2);
                str += (char) (a);
            }
            
            System.out.println(str);
            
            // exibe a mensagem no TextField receptor
            ScreenView.receiverTxt.setText(ScreenView.receiverTxt.getText() + str);
        } else if (framingType == 2 && encodingType == 1) {
            for (int i = 0; i < s.length() / 8; i++) {
                int a = Integer.parseInt(s.substring(8 * i, (i + 1) * 8), 2);
                str += (char) (a);
            }
            
            System.out.println(str);
            
            // exibe a mensagem no TextField receptor
            ScreenView.receiverTxt.setText(ScreenView.receiverTxt.getText() + str);
        } else if (framingType == 3 && encodingType == 1) {
            
            for (int i = 0; i < s.length() / 8; i++) {
                int a = Integer.parseInt(s.substring(8 * i, (i + 1) * 8), 2);
                str += (char) (a);
            }
            
            System.out.println(str);
            // exibe a mensagem no TextField receptor
            ScreenView.receiverTxt.setText(ScreenView.receiverTxt.getText() + str);
        } else {
            str = packets.bitsToString(packets);
            
            // exibe a mensagem no TextField receptor
            ScreenView.receiverTxt.setText(ScreenView.receiverTxt.getText() + str);
        }
        
        System.out.println("-----Codifications.receivingApplicationLayer-----");
    } // Fim do metodo receivingApplicationLayer
}