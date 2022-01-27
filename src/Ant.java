public class Ant {
    protected int trailSize;//number of features in the trail
    protected int trail[];// features that the ant has tried
    protected boolean discarded[];//features with less contribution
    protected boolean selected[];
    protected int antIndex;//each ant index may vary becauce of the descarding of the features
    protected int negativeCount[];
    protected double antSolution;//each ant will have a solution
    protected boolean isChild;
    protected double pheramone;//o
    protected int rank=0;
    protected int antFactor=0;


    public Ant(int numberOfFeatures)
    {
        this.trailSize = 0;//max number of features
        this.trail = new int[numberOfFeatures];//what all fetures are been traversed by this ant will be there in this array
        this.selected = new boolean[numberOfFeatures+1];//if the feture id is used in this ant the index will be set to true
        this.discarded = new boolean[numberOfFeatures+1];//if the feture is removed due to effecincey it will be mentioned here
        this.negativeCount= new int[numberOfFeatures];//??
        this.isChild=true;//Eventually new objects ants will be created from this pint so this object will be destroyed or will not be used
        this.pheramone=0;//we will increase the pheramone value for each cycle based on the success rate this will help the algo to pick which Ant gets chance to proceed
        this.antIndex=0;

    }
    protected double trailsolution()//this fuction will allow ant to calculate the solution
    {
        Solution solutionfrommachine =new Solution();
        double solution = solutionfrommachine.solutionfrommachinelearning(trail);
        antSolution=solution;
        return solution;
    }
    public void addToTrail(int newFeature)//will update the trail with the new feture
    {
        trail[antIndex]=newFeature;
        antIndex++;
    }
    protected void selectfeature( int feature)
    {
        trail[antIndex] = feature; //add to trail
        selected[feature] = true;           //update flag
        antIndex++;
    }

    protected boolean isSelected(int i)
    {
        return selected[i];
    }
    protected boolean isDiscared(int i)
    {
        return discarded[i];
    }
    protected void discardfeatue(int feature)
    {
        //finding the feture
        int indexOffeture=0;
        for(int i=0;i<antIndex;i++)
        {
            if(trail[i]==feature)
            {
                indexOffeture=i;
                break;
            }
        }
        for (int i=indexOffeture;i<antIndex;i++)
        {
            trail[i]=trail[i+1];
        }
        trail[antIndex]=0;
        antIndex--;//decreasing the Index
        discarded[feature]=true;

    }

    protected void clear()
    {
        for (int i = 0; i < trailSize; i++) {
            selected[i] = false;
            negativeCount[i] = 0;
            trail[i]=0;
            antSolution=0;
            antIndex=0;
            discarded[i]=false;
        }
    }

    public double getPheramone() {
        return pheramone;
    }

    public void setPheramone(double pheramone) {
        this.pheramone = pheramone;
    }

    public void setChild()
    {
        this.isChild=true;
    }
    public boolean isChildCheck()
    {
        if(isChild)
            return true;
        else
            return false;
    }
}
