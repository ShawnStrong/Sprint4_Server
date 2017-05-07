public class ResultRacer {
	public int Place;
	public int bibNumber;
	public String Name;
	public long Time;

	
	public ResultRacer (int i, int num, String alias, long tim) {
		
		this.Place = i;
		this.bibNumber = num;
		this.Name = alias;
		this.Time = tim;
	}

	public void print(){
		System.out.println(this.Place + " " + this.bibNumber + " " + this.Name + " " + this.Time);
	}
}