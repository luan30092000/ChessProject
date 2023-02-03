package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
                   final Collection<Move> legalMoves,
                   final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();   // Is the king under attack?
    }

    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : opponentMoves) {
            if (piecePosition == move.getDestinationCoordinate()) { // Piece is in opponent move
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Board have no king Exception"); // If no king was setting up, invalid board
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    //TODO implementing
    public boolean isCheck() {
        return false;
    }

    //TODO implementing
    public boolean isCheckMate() {
        return this.isInCheck;
    }

    //TODO implementing
    public boolean isStaleMate() {
        return !hasEscapeMove() && !this.isInCheck;
    }

    protected boolean hasEscapeMove() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public MoveTransition makeMove(final Move move) {

        if(!isMoveLegal(move)) {    // If move is illegal, return current board with ILLEGAL MOVE STATUS
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        // Board after the move
        final Board transitionBoard = move.execute();

        // Check if the move is gonna expose the king, lead to illegal move
        final Collection<Move> kingAttacks =  Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());
        if (!kingAttacks.isEmpty()) {   // Because exposing the king to attack, this return current board with LEAVE IN CHECK STATUS
            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
        }

        // Everything is in check, return pending board
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
}
