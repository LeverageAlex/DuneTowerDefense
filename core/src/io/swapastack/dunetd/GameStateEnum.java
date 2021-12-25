package io.swapastack.dunetd;

public enum GameStateEnum {
    BUILDING,
    WAVE,
    SELECTED;

    public static int gameState(GameStateEnum gs) {
        switch (gs) {
            case BUILDING:
                return 0;
            case WAVE:
                return 1;
            case SELECTED:
                return 3;
            default:
                return -1;
        }
    }


}
