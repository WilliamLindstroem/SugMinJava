package infiniteStairs;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;


public class StairCase {
    private final Player player;
    private final Random rng = new Random();


    private final int stairW = 70;
    private final int stairH = 50;

    private final int visibleAbove = 14;

    private final Map<Integer, StepData> steps = new HashMap<>();


    private int baseIndex = 0;

    private int baseGridX = 0;

    private double timeRemainingSeconds = 0.0;

    private int bestScore = 0;

    public StairCase(Player player) {
        this.player = player;
        reset();
    }

    public void reset(){

        steps.clear();

        baseIndex = 0;
        baseGridX = 0;

        steps.put(0, new StepData(baseGridX, randType()));

        ensureGeneratedUpTo(visibleAbove + 6);

        timeRemainingSeconds = timeLimitSeconds(getScore());

    }

    public void onStep() {

        int currentScore = getScore();

        timeRemainingSeconds = timeLimitSeconds(currentScore);

        int nextIndex = baseIndex + 1;
        ensureGeneratedUpTo(nextIndex + visibleAbove + 6);

        StepData next = steps.get(nextIndex);
        if (next == null) {
            reset();
            return;

        }

        int expectedGridX = baseGridX + (player.getDir() == 0 ? -1 : 1);

        if (next.gridX != expectedGridX) {
            bestScore = Math.max(bestScore, currentScore);
            reset();
            return;

        }

        baseIndex = nextIndex;
        baseGridX = next.gridX;

        timeRemainingSeconds = timeLimitSeconds(getScore());


    }

    public void update(double dtSeconds){
        if (getScore() <= 0) return;

        timeRemainingSeconds -= dtSeconds;
        if (timeRemainingSeconds <= 0.0) {
            bestScore = Math.max(bestScore, getScore());
            reset();
        }
    }

    public void draw(Graphics2D g2){
        int visibleBelow = 6;
        int start = Math.max(0, baseIndex - visibleBelow);
        int end = baseIndex + visibleAbove;
        ensureGeneratedUpTo(end + 2);

        for (int i = start; i <= end; i++){
            StepData s = steps.get(i);
            if (s == null) continue;

            int x = gridToScreenX(s.gridX);
            int y = indexToScreenY(i);

            if (y > GamePanel.H || y + stairH < 0) continue;

            if (s.type < 2) g2.setColor(Color.YELLOW);
            else g2.setColor(Color.GRAY);

            g2.fillRect(x,y,stairW,stairH);
        }
    }

    public void drawHud(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + getScore(), 20, 400);
        g2.drawString("Best: " + bestScore, 20, 600);

        int barX = 20;
        int barY = 50;
        int barW = Math.min(260, GamePanel.W - 40);
        int barH = 14;

        double limit = timeLimitSeconds(getScore());

        double ratio;
        if (getScore() <= 0) {
            ratio = 1.0;
        } else if (limit <= 0.0) {
            ratio = 0.0;
        } else {
            ratio = clamp01(timeRemainingSeconds / limit);

        }

        g2.setColor(Color.WHITE);
        g2.drawRect(barX,barY,barW,barH);

        int fillW = (int) Math.round(barW * ratio);
        g2.setColor(Color.RED);
        g2.fillRect(barX,barY,fillW,barH);

    }

    public int getScore(){
        return baseIndex;
    }

    private int randType() {
        return 1 + rng.nextInt(10);
    }

    private void ensureGeneratedUpTo(int targetIndex){
        for (int i = 1; i < targetIndex; i++) {
            if (steps.containsKey(i)) continue;

            StepData prev = steps.get(i - 1);
            int dir = rng.nextBoolean() ? 1 : -1;
            int gridX = prev.gridX + dir;

            steps.put(i, new StepData(gridX, randType()));
        }
    }



    private double timeLimitSeconds(int score){
        double base = 0.9;
        double decay = 0.985;
        double min = 0.18;
        return Math.max(min, base * Math.pow(decay, score));

    }

    private int gridToScreenX(int gridX){
        int playerCenterX = player.getX();
        int baseStepScreenX = playerCenterX ;
        int deltaGrid = gridX - baseGridX;
        return baseStepScreenX + deltaGrid * stairW;
    }

    private int indexToScreenY(int stepIndex) {
        int baseStepScreenY = player.getY() + player.getHeight();
        int delta = stepIndex - baseIndex;
        return baseStepScreenY - delta * stairH;
    }

    private static double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 0.0) return 1.0;
        return v;
    }

    private record StepData(int gridX, int type) {
    }
}
