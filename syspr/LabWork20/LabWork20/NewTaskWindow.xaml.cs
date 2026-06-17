using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace LabWork20
{
    /// <summary>
    /// Логика взаимодействия для NewTaskWindow.xaml
    /// </summary>
    public partial class NewTaskWindow : Window
    {
        public NewTaskWindow()
        {
            InitializeComponent();
        }

        private void BrowseButton_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "Исполняемые файлы (*.exe)|*.exe";

            if (openFileDialog.ShowDialog() is true)
                PathTextBox.Text = openFileDialog.FileName;
        }

        private void OkButton_Click(object sender, RoutedEventArgs e)
        {
            string path = PathTextBox.Text.Trim();

            if (string.IsNullOrEmpty(path))
            {
                MessageBox.Show("Введите имя файла или путь.");
                return;
            }

            try
            {
                Process.Start(path);
                Close(); 
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка: {ex.Message}");
            }
        }

        private void CancelButton_Click(object sender, RoutedEventArgs e)
            => Close();
    }
}
