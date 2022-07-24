package app.tapsoffire.library;

public class Score {

    public int score;
    public float rating;

    public Score(Score other) {
        this.score = other.score;
        this.rating = other.rating;
    }

    public Score(int score, float rating) {
        this.score = score;
        this.rating=rating;
    }

    public boolean isBetter(Score other) {
        return other == null ||
                (this.score > other.score) ||
                (this.score == other.score && this.rating > other.rating);
    }

}
