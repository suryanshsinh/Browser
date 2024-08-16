package application.types;

public class HistoryStack {
	History bottom;
	History top;
	
	public HistoryStack() {
		this.top = null;
		this.bottom = null;
	}
	
	void add(String url) {
		History newEntry = new History(url);
		if (top == null) {
			top = bottom = newEntry;
		} else {
			History temp = bottom;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = newEntry;
			top = newEntry;
		}
	}
	
	History pop() {
		if (top == null) {
			return null;
		} else if (top == bottom) {
			History temp = top;
			top = bottom = null;
			return temp;
		} else {
			History temp = bottom;
			while (temp.next.next != null) {
				temp = temp.next;
			}
			top = temp;
			temp = top.next;
			top.next = null;
			return temp;
		}
	}
	
	void display() {
		if (top != null) {
			History temp = bottom;
			while (temp.next != null) {
				System.out.print(temp.url + " - ");
				temp = temp.next;
			}
			System.out.println(temp.url);
		}
	}
}

class History {
	String url;
	History next;
	
	History(String url) {
		this.url = url;
		this.next = null;
	}
}