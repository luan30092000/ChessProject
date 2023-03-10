package com.chess.engine.board;

import com.chess.engine.player.MoveTransition;

import java.util.*;

/**
 * BoardUtils is just a utility class with static method which support the pieces
 * -> No need to initiate the class
 */

public class BoardUtils {

    public static final int NUM_TILES_PER_ROW = 8, NUM_TILES = 64, SIDE_MOVE = 1, START_TILE_INDEX = 0;
    /**
     * Bool array with specific element that are marked TRUE to check piece's special (edge) locations
     */
    public static final boolean[] FIRST_COLUMN = initColumn(0),
            SECOND_COLUMN = initColumn(1),
            SEVENTH_COLUMN = initColumn(6),
            EIGHTH_COLUMN = initColumn(7);

    public final List<String> ALGERBRAIC_NOTATION = initializeAlgebraicNotation();
    public final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    public static final boolean[] EIGHTH_RANK = initRow(0),
            SEVENTH_RANK = initRow(8),
            SIXTH_RANK = initRow(16),
            FIFTH_RANK = initRow(24),
            FOURTH_RANK = initRow(32),
            THIRD_RANK = initRow(40),
            SECOND_RANK = initRow(48),
            FIRST_RANK = initRow(56);


    /**
     * Make bool arr of 64 element and assigned True to specific edge elements
     * @param columnNumber the col num index 0 1 6 7 (knight's edge cases)
     * @return column to check
     */
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] table = new boolean[64];

        do {
            table[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < 64);
        return table;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] table = new boolean[64];

        do {
            table[rowNumber] = true;
            rowNumber += SIDE_MOVE;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return table;
    }

    private Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGERBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

    public String getPositionAtCoordinate(final int coordinate) {
        return ALGERBRAIC_NOTATION.get(coordinate);
    }

    private BoardUtils() {
        throw new RuntimeException("You cannot initiate this class");
    }

    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    public int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static boolean kingThreat(final Move move) {
        final Board board = move.getBoard();
        final MoveTransition transition = board.getCurrentPlayer().makeMove(move);
        return transition.getToBoard().getCurrentPlayer().isInCheck();
    }
}
