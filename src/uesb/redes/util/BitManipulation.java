package uesb.redes.util;

import java.util.ArrayList;
import java.util.List;

public class BitManipulation {
    
    private int size; // tamanho do array
    private int endSize; // ultima posicao array
    
    // cria um ArrayList de bytes
    List<Byte> bits = new ArrayList<>();
    
    public BitManipulation() {
        endSize--;
    }
    
    public void addBit(int bitValue) {
        endSize++; // ultima posicao aumenta
        if (endSize % 8 == 0) {
            bits.add((byte) 0);
        }
        
        int pos = (endSize / 8);
        int binary = (int) bits.get(pos);
        
        int value = 8 + (8 * pos) - endSize;
        
        if (bitValue == 1) {
            binary = (binary | (1 << 8 - value));
            byte v = (byte) binary;
            bits.set(pos, v);
        }
    } // fim do metodo addBit
    
    public void removeBit() {
        endSize--;
        
        int pos = ((endSize + 1) / 8);
        int binary = (int) bits.get(pos);

//        int value = 8 + (8 * pos) - endSize + 1;
        
        binary = (binary >> 1);
        byte v = (byte) binary;
        bits.set(pos, v);
    } // fim do removeBit
    
    public int size() {
        return endSize + 1;
    } // fim do metodo size
    
    public int get(int position) {
        byte aux;
        int pos = (position / 8);
        aux = bits.get(pos);
        position = position - (pos * 8);
        
        if ((aux >> (position) & 1) == 1) {
            return 1;
        } else {
            return 0;
        }
    } // fim do metodo get
    
    public void stringToBits(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i); // recebe o caractere da entrada
            int binary = (int) c; // define em codigo ASCII o valor do caractere
            for (int j = 7; j >= 0; j--) {
                if (((binary >> j) & 1) == 1) {
                    addBit(1);
                } else {
                    addBit(0);
                }
            } // fim do for
        } // fim do for
    } // fim do metodo stringToBits
    
    public String bitsToString(BitManipulation bits) {
        int last;
        String saida = "";
        for (int i = 0; i < bits.size() / 8; i++) {
            int binary = 0;
            last = i * 8;
            for (int j = 7; j >= 0; j--) {
                if (bits.get(last + j) == 1) {
                    binary = (binary | (1 << j));
                }
            } // fim do for
            int value = 0;
            int count = 0;
            
            while (count < 8) {
                value <<= 1;
                value |= (binary & 1);
                binary >>= 1;
                count++;
            } // fim do while
            char c = (char) value;
            saida = saida + c;
        }// fim do for
        
        return saida;
    } // fim do metodo bitsToString
    
    public void printBits() {
        String saida = "";
        for (int i = 0; i < size(); i++) {
            if (get(i) == 1) {
                saida += "1";
            } else {
                saida += "0";
            }
        } // fim do for
        System.out.println(saida);
    } // fim do metodo printBits
}// fim da classe Bits 