
/*
  Created by Hugo Teixeira Mafra <hugorc10@hotmail.com> on 20/08/2018. Last modification on 13/09/2018.
  <p>
  Enrollment number: 201611540.
  <p>
  Encoder it is a software that simulates a physical layer encoder.
  <p>
  Encoder is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  <p>
  Encoder is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.
  <p>
 */

package uesb.redes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import uesb.redes.view.ScreenView;

public class Principal extends Application {
    
    public static void main(String[] args) {
        launch(Principal.class);
    } // fim do metodo main
    
    @Override
    public void start(Stage myStage) {
        ScreenView screenView = new ScreenView();
    
        Scene scene = new Scene(screenView.crateContent());
        myStage.setScene(scene);
        myStage.setTitle("Physical Layer Encoder");
        // o tamanho da janela nao podera ser redimencionado
        myStage.setResizable(false);
        // a janela sempre estara maximizada
        myStage.setMaximized(true);
        myStage.show();
    } // fim do metodo start
} // fim da classe Principal
