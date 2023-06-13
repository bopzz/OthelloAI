import java.util.ArrayList;

public interface FormalModel<S, TURN>{
    public TURN toMove(S state);
    public ArrayList action(S state);
    public S result(S state, int[] move);
    public boolean isTerminal(S state);
    public int utility(S state, TURN turn);
}