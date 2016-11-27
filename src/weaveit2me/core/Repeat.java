package weaveit2me.core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Repeat{
		String name;
		List<Integer> plan;
		
		public Repeat(String name, ArrayList<Integer> plan) {
			setName(name);
			setPlan(plan);
		}
		
		public Repeat(String name){
			this.name = name;
			this.plan = new ArrayList<Integer>();
		}
		
		public Repeat(String name, int initialCapacity) {
			this.name = name;
			this.plan = new ArrayList<Integer>(initialCapacity);
		}

		public Repeat(String name, Integer[] arr1) {
			setName(name);
			setPlan(new ArrayList<Integer>(Arrays.asList(arr1)));
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Integer> getPlan() {
			return plan;
		}

		public void setPlan(List<Integer> plan) {
			this.plan = plan;
		}
		
		public void addShaftLiftPattern(Integer bitPattern){
			plan.add(bitPattern);
		}
		
		public void insertShaftLiftPattern(int index, Integer bitPattern) {
			plan.add(index, bitPattern);
		}
		
		public void deleteShaftLiftPattern(int index){
			plan.remove(index);
		}
		
	}
