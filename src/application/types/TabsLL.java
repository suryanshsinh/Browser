package application.types;

import java.io.IOException;

public class TabsLL {
	public NewTab first = null;
	public TabsLL listObj;
	
	public NewTab addNewTab() throws IOException {
		NewTab tab = new NewTab(listObj);
		tab.setOnClosed(event -> {
			if (tab == first) {
				first = tab.next;
			}
			tab.previous.next = tab.next;
			tab.next.previous = tab.previous;
		});
		if (first == null) {
			first = tab;
			first.previous = first;
			first.next = first;
			first.selected = true;
		} else {
			NewTab temp = first;
			temp.selected = false;
			while (temp.next != first) {
				temp = temp.next;
				temp.selected = false;
			}
			temp.next = tab;
			tab.previous = temp;
			tab.next= first;
			tab.selected = true;
			first.previous = tab;
		}
		return tab;
	}
	
	public boolean display() {
		if (first == null) {
			return false;
		}
		NewTab temp = first;
		while (temp.next != first) {
			System.out.print(temp.getText() + ":" + temp.selected + ", ");
			temp = temp.next;
		}
		System.out.println(temp.getText() + ":" + temp.selected);
		return true;
	}

	void deSelectAll() {
		NewTab temp = first;
		temp.selected = false;
		while (temp.next != first) {
			temp = temp.next;
			temp.selected = false;
		}
	}

	public NewTab selectNext() {
		NewTab temp = first;
		while (temp.selected != true) {
			temp = temp.next;
		}
		return temp.next;
	}
	public NewTab selectPrevious() {
		NewTab temp = first;
		while (temp.selected != true) {
			temp = temp.next;
		}
		return temp.previous;
	}
	NewTab getSelected() {
		NewTab temp = first;
		while (temp.selected != true) {
			temp = temp.next;
		}
		return temp;
	}
	public NewTab closeSelected() {
		NewTab temp = first;
		while (temp.selected != true) {
			temp = temp.next;
		}
		if (temp == first) {
			first = temp.next;
		}
		temp.previous.next = temp.next;
		temp.next.previous = temp.previous;
		return temp;
	}
	public void updateTabs() {
		NewTab temp = first;
		do  {
			temp.controller.updateMenuButtons();
			temp = temp.next;
		} while (temp != first);
	}
}
