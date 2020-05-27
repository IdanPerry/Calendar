package com.idan.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class represents an event editor, which handles the event user input.
 * 
 * @author Idan Perry
 * @version 24.05.2020
 */

@SuppressWarnings("serial")
public class EventEditor extends JFrame implements ActionListener, WindowListener {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 200;
	private static final int EXPANSION_HEIGHT = 380;
	private static final int DETAILS_ROWS = 8;
	private static final int DETAILS_COLS = 20;
	
	private static final Font EVENT_FONT = new Font("Sans Serif", Font.PLAIN, 18);
	private static final Font BTN_FONT = new Font("Calibri", Font.BOLD, 16);
	private static final Font EVENT_TITLE = new Font("", Font.PLAIN, 32);
	private static final Font TIME_FONT = new Font("Sans Serif", Font.BOLD, 16);
	private static final Dimension EDIT_FIELD_SIZE = new Dimension(270, 28);
	private static final Color RED = new Color(153, 0, 0);
	private static final String[] TIME = { "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30",
			"10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
			"16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
			"22:00", "22:30", "23:00", "23:30" };

	private JScrollPane scroll;
	private JPanel mainEditPanel;	// main panel - holding all north region of the editor
	private JPanel iconsPanel;		// holds the icons representing the edit fields
	private JPanel eventPanel; 		// holds the event haeder and time editabels
	private JPanel eventEditPanel;  // sub-panel holds the event textField
	private JPanel eventTimePanel;  // sub-panel holds the event time edit
	private JPanel btnsPanel; 		// sub-panel holds the buttons
	private JPanel detailsPanel;	// holds the details text area
	private JTextField eventField;
	private JLabel eventTitleLbl;
	private JLabel eventTimeFromLbl;
	private JLabel eventTimeToLbl;
	private JLabel clockIconLbl;
	private JComboBox<String> timeFrom;
	private JComboBox<String> timeTo;
	private JTextArea detailsArea;
	private JButton saveBtn;
	private JButton detailsBtn;
	private CalendarDay caller; 	// the day panel in the calendar which called this editor
	private MyCalendar calendar;
	private String date;
	private int eventIndex;
	private boolean newEvent;

	/**
	 * Constructs an event editor window, which handles the event
	 * user input.
	 * 
	 * @param calendar the calendar in which to add or edit an event.
	 */
	public EventEditor(MyCalendar calendar) {
		super("Event Editor");
		this.calendar = calendar;
		mainEditPanel = new JPanel(new BorderLayout());
		iconsPanel = new JPanel(new GridLayout(2, 1));
		eventPanel = new JPanel(new GridLayout(2, 1));
		eventEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		eventTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		detailsPanel = new JPanel();
		btnsPanel = new JPanel();
		eventField = new JTextField("Add an event");
		eventTitleLbl = new JLabel("   \u270D");
		eventTimeFromLbl = new JLabel("From");
		eventTimeToLbl = new JLabel("to");
		clockIconLbl = new JLabel("   \u23F1  ");
		timeFrom = new JComboBox<String>(TIME);
		timeTo = new JComboBox<String>(TIME);
		detailsArea = new JTextArea("Add more details", DETAILS_ROWS, DETAILS_COLS);
		scroll = new JScrollPane(detailsArea);
		saveBtn = new JButton("Save");
		detailsBtn = new JButton("Add details");

		setSize(WIDTH, HEIGHT);
		getContentPane().setBackground(MyCalendar.LIGHT_BLACK);
		setResizable(false);

		addWindowListener(this);
		detailsBtn.addActionListener(this);
		saveBtn.addActionListener(this);

		buildComponentsHierarchy();
		customizeComponents();
		validate();
	}

	/**
	 * Returns the event title field text of this window.
	 * 
	 * @return the event title field text of this window
	 */
	public JTextField getEventField() {
		return eventField;
	}

	/**
	 * Returns the event details text area of this window.
	 * 
	 * @return the event details text area of this window
	 */
	public JTextArea getDetailsArea() {
		return detailsArea;
	}

	/**
	 * Sets the CalendarDay object which called this event editor.
	 * 
	 * @param caller the CalendarDay object which called this event editor
	 */
	public void setCaller(CalendarDay caller) {
		this.caller = caller;
	}

	/**
	 * Returns true if the added event is new, otherwise returns false.
	 * 
	 * @return true if the added event is new, otherwise returns false.
	 */
	public boolean isNewEvent() {
		return newEvent;
	}

	/**
	 * Changes whether the added event is new or not, in which case is being edited.
	 * 
	 * @param newEvent true if the added event is new, false otherwise
	 */
	public void setNewEvent(boolean newEvent) {
		this.newEvent = newEvent;
	}
	
	/**
	 * Initializes a new event display.
	 */
	public void displayNewEvent() {
		eventField.setText("Add an event");
		timeFrom.setSelectedItem("06:00");
		timeTo.setSelectedItem("06:00");
		detailsArea.setText("Add more datails");
		
		setVisible(true);
	}
	
	/**
	 * Displays an event in a calendar from a specified date and event index.
	 * 
	 * @param date the date from which to retrieve the event
	 * @param i the index of the event in the event list of the specified date
	 */
	public void displayEvent(String date, int i) {
		this.date = date;
		this.eventIndex = i;
		
		eventField.setText(calendar.getEvants(date)[i].getHeader());
		timeFrom.setSelectedItem((String) calendar.getEvants(date)[i].getStartTime());
		timeTo.setSelectedItem((String) calendar.getEvants(date)[i].getEndTime());
		detailsArea.setText(calendar.getEvants(date)[i].getDetails());
		
		setVisible(true);
	}
	
	/*
	 * Builds the structure of this window's components.
	 */
	private void buildComponentsHierarchy() {
		// add components to sub-panels
		eventEditPanel.add(eventField);
		eventTimePanel.add(eventTimeFromLbl);
		eventTimePanel.add(timeFrom);
		eventTimePanel.add(eventTimeToLbl);
		eventTimePanel.add(timeTo);
		detailsPanel.add(scroll);
		btnsPanel.add(detailsBtn);
		btnsPanel.add(saveBtn);

		// add sub-panels to main panels
		eventPanel.add(eventEditPanel);
		eventPanel.add(eventTimePanel);		
		iconsPanel.add(eventTitleLbl);
		iconsPanel.add(clockIconLbl);		
		mainEditPanel.add(eventPanel, BorderLayout.CENTER);
		mainEditPanel.add(iconsPanel, BorderLayout.WEST);

		// add main panels to this frame		
		add(mainEditPanel, BorderLayout.NORTH);
		add(btnsPanel, BorderLayout.SOUTH);
	}

	/*
	 * Sets the properties for this window's components.
	 */
	private void customizeComponents() {
		// background color
		mainEditPanel.setBackground(MyCalendar.LIGHT_BLACK);
		iconsPanel.setBackground(MyCalendar.LIGHT_BLACK);
		eventPanel.setBackground(MyCalendar.LIGHT_BLACK);
		eventEditPanel.setBackground(MyCalendar.LIGHT_BLACK);
		eventTimePanel.setBackground(MyCalendar.LIGHT_BLACK);
		btnsPanel.setBackground(MyCalendar.LIGHT_BLACK);
		detailsPanel.setBackground(MyCalendar.LIGHT_BLACK);
		scroll.setForeground(Color.WHITE);
		timeFrom.setBackground(Color.WHITE);
		timeTo.setBackground(Color.WHITE);
		detailsBtn.setBackground(Color.GRAY);
		saveBtn.setBackground(RED);

		// text color
		eventTitleLbl.setForeground(Color.WHITE);
		clockIconLbl.setForeground(Color.WHITE);
		eventTimeToLbl.setForeground(Color.WHITE);
		eventTimeFromLbl.setForeground(Color.WHITE);
		detailsBtn.setForeground(Color.WHITE);
		saveBtn.setForeground(Color.WHITE);

		// fonts
		eventTitleLbl.setFont(EVENT_TITLE);
		clockIconLbl.setFont(EVENT_TITLE);
		eventField.setFont(EVENT_FONT);
		eventTimeFromLbl.setFont(TIME_FONT);
		eventTimeToLbl.setFont(TIME_FONT);
		timeFrom.setFont(TIME_FONT);
		timeTo.setFont(TIME_FONT);
		detailsArea.setFont(EVENT_FONT);
		detailsBtn.setFont(BTN_FONT);
		saveBtn.setFont(BTN_FONT);

		eventField.setPreferredSize(EDIT_FIELD_SIZE);
		detailsArea.setMargin(new Insets(0, 10, 0, 10));
	}

	/*
	 * Creates a new CalendarEvent object once the save button is clicked,
	 * if a new event has been added. otherwise modifies an existing event.
	 */
	private void saveEvent() {
		String date = calendar.getYear() + "." + calendar.getMonth() + "." + caller.getDayOfMonth();
		CalendarEvent event = new CalendarEvent(eventField.getText(), detailsArea.getText(),
				(String) timeFrom.getSelectedItem(), (String) timeTo.getSelectedItem());

		if (newEvent) {			
			calendar.addCalendarEvent(date, event);
			caller.setEvent(event);
			
		} else {
			calendar.modifyCalendarEvent(this.date, eventIndex, event);
			caller.modifyEvent(event, eventIndex);
		}
	}
	
	/*
	 * Restores the original size of this window.
	 */
	private void shrink() {
		remove(detailsPanel);
		setSize(WIDTH, HEIGHT);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveBtn) {
			saveEvent();
			shrink();
			setVisible(false);
			validate();
		}

		if (e.getSource() == detailsBtn) {
			add(detailsPanel, BorderLayout.CENTER);
			setSize(WIDTH, EXPANSION_HEIGHT);
			validate();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		shrink();
		setVisible(false);
		validate();
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
