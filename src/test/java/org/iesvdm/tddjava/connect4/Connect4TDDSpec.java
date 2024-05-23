package org.iesvdm.tddjava.connect4;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;
import static org.iesvdm.tddjava.connect4.Connect4.*;
import static org.junit.jupiter.api.Assertions.*;


public class Connect4TDDSpec {

    private Connect4TDD tested;

    private OutputStream output;

    @BeforeEach
    public void beforeEachTest() {
        output = new ByteArrayOutputStream();

        //Se instancia el juego modificado para acceder a la salida de consola
        tested = new Connect4TDD(new PrintStream(output));
    }

    /*
     * The board is composed by 7 horizontal and 6 vertical empty positions
     */

    @Test
    public void whenTheGameStartsTheBoardIsEmpty() {
       // tested.getNumberOfDiscs();
        assertThat(tested.getNumberOfDiscs()).as("Debe ser cero").isNotZero();

    }

    /*
     * Players introduce discs on the top of the columns.
     * Introduced disc drops down the board if the column is empty.
     * Future discs introduced in the same column will stack over previous ones
     */
    // Cambio
    @Test
    public void whenDiscOutsideBoardThenRuntimeException() {

        assertThatThrownBy(() -> tested.putDiscInColumn(-1))
                .isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> {
            for (int i = 0; i <= 7; i++) {
                tested.putDiscInColumn(3);
                tested.putDiscInColumn(4);
            }
        }).isInstanceOf(RuntimeException.class).hasMessageContaining("No more room");

        assertThrows(RuntimeException.class, () -> {
            for (int i = 0; i <= 7; i++) {
                tested.putDiscInColumn(5);
            }
        });

    }

    @Test
    public void whenFirstDiscInsertedInColumnThenPositionIsZero() {

        for (int i = 0; i <= 6; i++) {
            assertThat(tested.putDiscInColumn(i)).isEqualTo(0);
        }

    }

    @Test
    public void whenSecondDiscInsertedInColumnThenPositionIsOne() {

        for (int i = 0; i <= 6; i++) {
            tested.putDiscInColumn(i);
            assertThat(tested.putDiscInColumn(i)).isEqualTo(1);
        }

    }

    @Test
    public void whenDiscInsertedThenNumberOfDiscsIncreases() {

        int discosIniciales = 0;
        tested.putDiscInColumn(3);
        assertThat(tested.getNumberOfDiscs())
                .isEqualTo(discosIniciales+1);

        tested.putDiscInColumn(3);
        assertThat(tested.getNumberOfDiscs())
                .isEqualTo(discosIniciales+2);
    }

    @Test
    public void whenNoMoreRoomInColumnThenRuntimeException() {
        //Llenamos la columna 1
        for (int i = 0; i <= 5; i++) {
            tested.putDiscInColumn(1);
        }

        // Una vez llena la columna si metemos una ficha mas nos saltara la excepecion
        assertThatThrownBy(() -> tested.putDiscInColumn(1))
                .isInstanceOf(RuntimeException.class);

    }

    /*
     * It is a two-person game so there is one colour for each player.
     * One player uses red ('R'), the other one uses green ('G').
     * Players alternate turns, inserting one disc every time
     */

    @Test
    public void whenFirstPlayerPlaysThenDiscColorIsRed() {
        //Como no hemos tocado nada el primer jugador por defecto es el rojo:

        assertEquals(tested.getCurrentPlayer(), "R");

    }

    @Test
    public void whenSecondPlayerPlaysThenDiscColorIsGreen() {
        //Insertamos un disco
        tested.putDiscInColumn(1);
        //Ahora que estamos en el turno 2 el jugador deberia ser GREEN
        assertEquals(tested.getCurrentPlayer(), "G");
    }

    /*
     * We want feedback when either, event or error occur within the game.
     * The output shows the status of the board on every move
     */

    @Test
    public void whenAskedForCurrentPlayerTheOutputNotice() {
        String currentPlayer = tested.getCurrentPlayer();
        String mensajeOutput = String.format("Player %s turn%n", currentPlayer);
        assertThat(output.toString()).isEqualTo(mensajeOutput);


    }

    @Test
    public void whenADiscIsIntroducedTheBoardIsPrinted() {
        tested.putDiscInColumn(0);

        String expected =
                "| | | | | | | |\n" +
                "| | | | | | | |\n" +
                "| | | | | | | |\n" +
                "| | | | | | | |\n" +
                "| | | | | | | |\n" +
                "|R| | | | | | |\n";

        assertThat(output.toString()).isEqualTo(expected);
    }

    /*
     * When no more discs can be inserted, the game finishes and it is considered a draw
     */

    @Test
    public void whenTheGameStartsItIsNotFinished() {

    }

    @Test
    public void whenNoDiscCanBeIntroducedTheGamesIsFinished() {
        String expected ="|G|G|G|R|G|G|G|\n"+
                            "|R|R|R|G|R|R|R|\n"+
                            "|G|G|G|R|G|G|G|\n"+
                            "|R|R|R|G|R|R|R|\n"+
                            "|G|G|G|R|G|G|G|\n"+
                            "|R|R|R|G|R|R|R|\n"+
                            "Current turn: R\n"+
                            "It's a draw\n";
        for (int i = 0; i <= 2; i++){
            for (int j = 0; j <= 5; j++) {
                tested.putDiscInColumn(i);
            }
        }
        for (int i = 4; i <= 6; i++){
            for (int j = 0; j <= 5; j++) {
                if (i != 6 || j != 5) {
                    tested.putDiscInColumn(i);
                }
            }
        }
        for (int i = 3; i <= 3; i++){
            for (int j = 0; j <= 5; j++) {
                tested.putDiscInColumn(i);
            }
        }
        tested.putDiscInColumn(6);
        assertTrue(tested.isFinished());
    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight vertical line then that player wins
     */

    @Test
    public void when4VerticalDiscsAreConnectedThenThatPlayerWins() {
        for (int i = 0; i < 4; i++) {
            tested.putDiscInColumn(0); // R
            if (i < 3) {
                tested.putDiscInColumn(1); // G
            }
        }
        assertThat(tested.getWinner()).isEqualTo("R");
    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight horizontal line then that player wins
     */

    @Test
    public void when4HorizontalDiscsAreConnectedThenThatPlayerWins() {
        for (int i = 0; i < 4; i++) {
            tested.putDiscInColumn(i); // R
            if (i < 3) {
                tested.putDiscInColumn(i); // G
            }
        }
        assertThat(tested.getWinner()).isEqualTo("R");
    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight diagonal line then that player wins
     */

    @Test
    public void when4Diagonal1DiscsAreConnectedThenThatPlayerWins() {
        int[] moves = {0, 1, 1, 2, 3, 2, 3, 3, 4, 3};
        for (int move : moves) {
            tested.putDiscInColumn(move);
        }
        assertThat(tested.getWinner()).isEqualTo("R");
    }

    @Test
    public void when4Diagonal2DiscsAreConnectedThenThatPlayerWins() {
        int[] moves = {3, 4, 4, 5, 5, 5, 6, 6, 6, 6};
        for (int move : moves) {
            tested.putDiscInColumn(move);
        }
        assertThat(tested.getWinner()).isEqualTo("R");
    }
}
