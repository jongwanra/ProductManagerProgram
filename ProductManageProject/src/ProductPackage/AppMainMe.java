package ProductPackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppMainMe {

	static class MyGUI extends JFrame {
		ProductDAOMe dao = new ProductDAOMe();
		ProductMe product;
		// 몰랏음
		ArrayList<ProductMe> datas = new ArrayList<ProductMe>();
		// 여기까지

		boolean editmode = false;
		protected JLabel m1, m2, m3, m4, m5;
		protected JPanel p1, p2, p3;
		protected JTextArea ta;
		protected JScrollPane scroll;
		protected JComboBox cb;
		protected JButton b1, b2, b3;

		MyActionListener listener;

		String msg = "## 메시지 : ";
		JTextField t1, t2, t3;

		public MyGUI() {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			m1 = new JLabel(msg + "프로그램이 시작되었습니다!!!");
			m2 = new JLabel("관리번호");
			m3 = new JLabel("상품명");
			m4 = new JLabel("단가");
			m5 = new JLabel("제조사");
			p1 = new JPanel();
			p2 = new JPanel();
			p3 = new JPanel();

			ta = new JTextArea(10, 40);
			scroll = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			cb = new JComboBox();
			t1 = new JTextField();
			t2 = new JTextField();
			t3 = new JTextField();

			listener = new MyActionListener();

			b1 = new JButton("등록");
			b2 = new JButton("조회");
			b3 = new JButton("삭제");

			// 내가 몰랐던 부분
			cb.addActionListener(listener);
			// 여기까지
			b1.addActionListener(listener);
			b2.addActionListener(listener);
			b3.addActionListener(listener);

			add(m1, BorderLayout.PAGE_START);

			GridLayout grl = new GridLayout(4, 1);
			p1.setLayout(grl);
			p1.add(m2);
			p1.add(m3);
			p1.add(m4);
			p1.add(m5);
			add(p1, BorderLayout.LINE_START);

			p2.setLayout(grl);
			p2.add(cb);
			p2.add(t1);
			p2.add(t2);
			p2.add(t3);
			add(p2, BorderLayout.CENTER);
			add(scroll, BorderLayout.LINE_END);
			p3.add(b1);
			p3.add(b2);
			p3.add(b3);
			add(p3, BorderLayout.PAGE_END);
			setSize(700, 400);
			setTitle("Product Manager");
			setVisible(true);
			// 몰랐던 부분
			refreshData();
			// 여기까지
		}

		public void clearField() {
			t1.setText("");
			t2.setText("");
			t3.setText("");
		}

		public void refreshData() {
			ta.setText("");
			clearField();
			editmode = false;

			ta.append("관리번호\t상품명\t\t단가\t제조사\n");
			datas = dao.getAll();

			// 데이터를 변경하면 콤보박스 데이터 갱신
			cb.setModel(new DefaultComboBoxModel(dao.getItems()));

			if (datas != null) {
				// ArrayList의 전체 데이터를 형식에 맞춰 출력
				for (ProductMe p : datas) {
					StringBuffer sb = new StringBuffer();
					sb.append(p.getPrcode() + "\t");
					sb.append(p.getPrname() + "\t\t");
					sb.append(p.getPrice() + "\t");
					sb.append(p.getManufacture() + "\n");
					ta.append(sb.toString());
				}
			} else {
				ta.append("등록된 상품이 없습니다. !!\n상품을 등록해 주세요 !!");

			}
		}

		class MyActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == b1) {
					product = new ProductMe();
					product.setPrname(t1.getText());
					product.setPrice(Integer.parseInt(t2.getText()));
					product.setManufacture(t3.getText());

					// 수정일 때
					if (editmode == true) {
						product.setPrcode(Integer.parseInt((String) cb.getSelectedItem()));
						if (dao.updateProduct(product)) {
							m1.setText(msg + "상품을 수정했습니다!!");
							clearField();
							editmode = false;
						} else
							m1.setText(msg + "상품 수정이 실패했습니다!!");

					}

					// 등록일 때
					else {
						if (dao.newProduct(product)) {
							m1.setText(msg + "상품을 등록했습니다!!");
						} else
							m1.setText(msg + "상품 등록이 실패했습니다!!");
					}
					// 화면 데이터 갱신
					refreshData();
				}
				// 조회 버튼 일 때
				else if (obj == b2) {
					String s = (String) cb.getSelectedItem();
					if (!s.equals("전체")) {
						product = dao.getProduct(Integer.parseInt(s));
						if (product != null) {
							m1.setText(msg + "상품정보를 가져왔습니다!!");
							t1.setText(product.getPrname());
							t2.setText(String.valueOf(product.getPrice()));
							t3.setText(product.getManufacture());
							// cb.setSelectedIndex(anIndex);
							editmode = true;
						} else {
							m1.setText(msg + "상품이 검색되지 않았습니다!!");
						}
					}
				} else if (obj == b3) {
					String s = (String) cb.getSelectedItem();
					if (s.equals("전체")) {
						m1.setText(msg + "전체는 삭제 되지 않습니다!!!");
					}
					else {
						if(dao.delProduct(Integer.parseInt(s))){
							m1.setText(msg + "상품이 삭제되었습니다.!!");
						}
						else {
							m1.setText(msg + "상품이 삭제되지 않았습니다.!!");
						}
					}
					refreshData();

				}

			}
		}

	}

	public static void main(String[] args) {
		new MyGUI();

	}

}
