import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JSlider levelSlider;
    private JLabel levelValueLabel;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;

    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PeopleMenu() {
        listPerson = new ArrayList<>();
        populateList();

        peopleTable.setModel(setTable());

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        String[] kategoriData = {"???", "Mahasiswa", "Dosen", "Staf", "Alumni"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // pengaturan slider (atribut baru)
        levelSlider.setMinimum(1);
        levelSlider.setMaximum(10);
        levelSlider.setValue(5);
        levelValueLabel.setText("5");
        levelSlider.addChangeListener(e ->
                levelValueLabel.setText(String.valueOf(levelSlider.getValue())));

        deleteButton.setVisible(false);

        // tombol add/update
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // tombol delete + konfirmasi
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah kamu yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // tombol cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // klik baris tabel
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = peopleTable.getSelectedRow();
                if (row == -1) return;
                selectedIndex = row;

                String curId = peopleTable.getModel().getValueAt(row, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(row, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(row, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(row, 4).toString();
                int curLevel = Integer.parseInt(peopleTable.getModel().getValueAt(row, 5).toString());

                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                levelSlider.setValue(curLevel);

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Tahun Lahir", "Kategori", "Level"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                    i + 1,
                    listPerson.get(i).getId(),
                    listPerson.get(i).getNama(),
                    listPerson.get(i).getTahunLahir(),
                    listPerson.get(i).getKategori(),
                    listPerson.get(i).getLevel()
            };
            tmp.addRow(row);
        }
        return tmp;
    }

    public void insertData() {
        try {
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int level = levelSlider.getValue();

            if (id.isEmpty() || nama.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null,
                        "ID, Nama, dan Kategori harus diisi!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            listPerson.add(new Person(id, nama, tahunLahir, kategori, level));
            peopleTable.setModel(setTable());
            clearForm();

            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int level = levelSlider.getValue();

            if (id.isEmpty() || nama.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null,
                        "ID, Nama, dan Kategori harus diisi!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Person p = listPerson.get(selectedIndex);
            p.setId(id);
            p.setNama(nama);
            p.setTahunLahir(tahunLahir);
            p.setKategori(kategori);
            p.setLevel(level);

            peopleTable.setModel(setTable());
            clearForm();

            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        listPerson.remove(selectedIndex);
        peopleTable.setModel(setTable());
        clearForm();

        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        levelSlider.setValue(5);

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    private void populateList() {
        listPerson.add(new Person("P001", "Budi Santoso", 2003, "Mahasiswa", 6));
        listPerson.add(new Person("P002", "Siti Aminah", 1985, "Dosen", 9));
        listPerson.add(new Person("P003", "Rian Pratama", 1990, "Staf", 7));
        listPerson.add(new Person("P004", "Dewi Lestari", 1998, "Alumni", 8));
        listPerson.add(new Person("P005", "Ahmad Fauzi", 2004, "Mahasiswa", 4));
    }
}