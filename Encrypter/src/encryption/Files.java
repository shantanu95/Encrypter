package encryption;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Files extends JPanel implements ActionListener{
	
	Comp c1, c2;
	static Thread en, de;
	JLabel label;
	JButton b1, b2;
	String s="", f="";
	Vector<Integer> bl=new Vector<Integer>();
	Vector<Integer> wi=new Vector<Integer>();
	Vector<Integer> el=new Vector<Integer>();
	static File file;
	static FileInputStream fis;
	static FileOutputStream fos;
	static BufferedOutputStream bos;
	static BufferedInputStream bis; 
	
	public Files() {
		super();
		c1=new Comp("Input File", FileDialog.LOAD);
		c2=new Comp("Output File", FileDialog.SAVE);
		label=new JLabel("Progress: ");
		b1=new JButton("Encrypt");
		b2=new JButton("Decrypt");
		b1.addActionListener(this);
		b2.addActionListener(this);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createRigidArea(new Dimension(0, 40)));
		add(c1);
		add(Box.createRigidArea(new Dimension(0, 25)));
		add(c2);
		add(b1);
		add(b2);
		add(Box.createRigidArea(new Dimension(0, 25)));
		add(label);
		add(Box.createVerticalGlue());
		this.setBackground(Color.WHITE);
	}
	
	class Comp extends JPanel implements ActionListener{
		
		int i;
		JTextField ta;
		JButton b;
		Comp(String s, int i){
			super();
			this.i=i;
			JLabel l=new JLabel(s);
			ta=new JTextField(15);
			ta.setMaximumSize(new Dimension(ta.getMinimumSize().width+150, ta.getMinimumSize().height));
			ta.setEditable(false);
			b=new JButton("Browse");
			b.addActionListener(this);
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			add(Box.createHorizontalGlue());
			add(l);
			add(Box.createRigidArea(new Dimension(0, 25)));
			add(ta);
			add(Box.createRigidArea(new Dimension(0, 25)));
			add(b);
			add(Box.createHorizontalGlue());
			this.setBackground(Color.WHITE);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(i==FileDialog.LOAD){
				FileDialog fdiag=new FileDialog(Encrypter.encr, "Select an input file", i);
				fdiag.setVisible(true);
				if(fdiag.getFile()!=null){
					String s=fdiag.getDirectory()+fdiag.getFile();
					file=new File(s);
					try {
						fis=new FileInputStream(file);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					c1.ta.setText(s);
				}	
			}else{
				FileDialog fdiag=new FileDialog(Encrypter.encr, "Select file", i);
				fdiag.setVisible(true);
				if(fdiag.getFile()!=null){
					String s=fdiag.getDirectory()+fdiag.getFile();
					try {
						fos=new FileOutputStream(s);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					c2.ta.setText(s);
				}	
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		s="";
		if((c1.ta.getText()!=null || c1.ta.getText()!="") && (c2.ta.getText()!=null || c2.ta.getText()!="")){
		if(ae.getSource()==b1){
			try {
				bis=new BufferedInputStream(fis);
				bos=new BufferedOutputStream(fos);
				int i;
				while((i=bis.read())!=-1)
					s=s+(char)i;
				Encrypt a=new Encrypt();
				en=new Thread(a);
				en.start();
				bis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try{
				bis=new BufferedInputStream(fis);
				bos=new BufferedOutputStream(fos);
				int i;
				while((i=bis.read())!=-1)
					s=s+(char)i;
				bis.close();
				Decrypt a=new Decrypt();
				de=new Thread(a);
				de.start();
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	}
	
	int genAdd(int elno, int width, int block, int pChar, int nChar){
		long l=(long) ((long)(Math.pow(elno, 6)+Math.pow(width, 5)+Math.pow(block, 3))
		+Math.pow(pChar, 3)+Math.pow(nChar, 2)+elno*nChar*elno*width+width*block+
		nChar*block+block*elno+pChar*elno+127);
		int i=(int)(l%256);
		return i;
	}
	
	int genWidth(int b){
		String s="9864943745774539577776593776755384674878467669445735444568757745563"
			+"56485778577734575778964977363777875746844494755495343474654958537745479"
			+"5579689558953545479956";
		int k=b%s.length();
		Integer i=Integer.parseInt(""+s.charAt(k));
		return i.intValue();
	}
	
	void Addition(){
		String f="";
		int b=0;
		int len=s.length();
		int k=0;
		for( ; k<s.length();){
			b++;
			int w=genWidth(b);
			if(w<=len){
				for(int i=0; i<w; i++){
					int l;
					if(k==0){
						l=genAdd(i+1, w, b, 0, sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}	
					else if(k==s.length()-1){
						l=genAdd(i+1, w, b, sumString(f.substring(0, f.length())), 0);
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}else{
						l=genAdd(i+1, w, b, sumString(f.substring(0, f.length())), sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}
					k++;
				}
				len=len-w;
			}else{
				for(int i=0; i<len; i++){
					int l;
					if(k==0){
						l=genAdd(i+1, w, b, 0, sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}	
					else if(k==s.length()-1){
						l=genAdd(i+1, w, b, sumString(f.substring(0, f.length())), 0);
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}else{
						l=genAdd(i+1, w, b, sumString(f.substring(0, f.length())), sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}
					k++;
				}
			}
		}
		try{
		for(int q=0; q<f.length(); q++)
			bos.write(f.charAt(q));
		bos.close();
		label.setText("File Written");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String invert(String s){
		String f="";
		for(int i=s.length()-1; i>=0; i--){
			f=f+s.charAt(i);
		}
		return f;
	}
	
	int sumString(String s){
		int k=0;
		for(int i=0; i<s.length(); i++){
			k=k+(int)s.charAt(i);
		}
		return k;
	}
	
	void setVectors(){
		int k=0;
		int b=0;
		int len=s.length();
		for( ; k<s.length();){
			b++;
			int w=genWidth(b);
			if(w<=len){
				for(int i=0; i<w; i++){
					bl.insertElementAt(new Integer(b), 0);
					wi.insertElementAt(new Integer(w), 0);
					el.insertElementAt(new Integer(i+1), 0);
					k++;
				}
				len=len-w;
			}else{
				for(int i=0; i<len; i++){
					bl.insertElementAt(new Integer(b), 0);
					wi.insertElementAt(new Integer(w), 0);
					el.insertElementAt(new Integer(i+1), 0);
					k++;
				}
			}
		}
	}
	
	void Subtraction(){
		f="";
		char c;
		int b=0;
		int len=s.length();
		setVectors();
		String s1=invert(s);
		int k=0;
		for( ; k<s.length();){
			b++;
			int w=genWidth(b);
			if(w<=len){
				for(int i=0; i<w; i++){
					int l;
					if(k==0){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), 0);
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}
					else if(k==s.length()-1){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), 0, sumString(f.substring(0, f.length())));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}else{
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), sumString(f.substring(0, f.length())));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}
					k++;
				}
				len=len-w;
			}else{
				for(int i=0; i<len; i++){
					int l;
					if(k==0){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), 0);
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}
					else if(k==s.length()-1){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), 0, sumString(f.substring(0, f.length())));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}else{
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), sumString(f.substring(0, f.length())));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
						label.setText("Progress: "+(k*100)/s.length()+"%");
					}
					k++;
				}
			}
		}
		String f1=invert(f);
		try{
			for(int q=0; q<f1.length(); q++)
				bos.write(f1.charAt(q));
			label.setText("File Written");
			bos.close();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	class Encrypt implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Addition();
		}
		}
	
	class Decrypt implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Subtraction();
		}
		
	}

}
