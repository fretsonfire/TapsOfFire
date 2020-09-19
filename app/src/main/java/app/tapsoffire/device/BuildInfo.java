package app.tapsoffire.device;


public class BuildInfo {

    private BuildType buildType;

    public BuildInfo(BuildType buildType) {
        this.buildType = buildType;
    }

    public BuildType getBuildType() {
        return this.buildType;
    }

}
