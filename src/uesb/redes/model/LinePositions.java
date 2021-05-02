package uesb.redes.model;

public enum LinePositions {
    // define a largura da linha do clock
    CLOCK_LINE_WIDTH(60),
    // posicao do eixo vertical que ira representar a transicao do sinal positivo
    POSITIVE_SIGNAL_Y(250),
    // posicao do eixo vertical que ira representar a transicao do sinal negativo
    NEGATIVE_SIGNAL_Y(350),
    // posicao inicial da animacoes das linhas no eixo vertical
    STREAM_X(250);
    
    private int value;
    
    LinePositions(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
