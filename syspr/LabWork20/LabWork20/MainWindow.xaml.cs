using System.Diagnostics;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace LabWork20
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        const int UpdateInterval = 2;
        private DispatcherTimer _timer;

        public MainWindow()
        {
            InitializeComponent();

            InitTimer();
            RefreshData();
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
            => RefreshData();

        private void InitTimer()
        {
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromSeconds(UpdateInterval);
            _timer.Tick += (s, e) => RefreshData();
            _timer.Start();
        }

        private void Refresh_Click(object sender, RoutedEventArgs e) 
            => RefreshData();

        private void RefreshData()
        {
            try
            {
                Process[] processes = Process.GetProcesses();

                var procList = processes.Select(p => new
                {
                    Name = p.ProcessName,
                    Id = p.Id,
                    Memory = (p.WorkingSet64 / 1024 / 1024).ToString("N0")
                }).OrderBy(p => p.Name).ToList();

                ProcessListView.ItemsSource = procList;
                StatusProcessCount.Text = procList.Count.ToString();

                var appList = processes
                    .Where(p => !string.IsNullOrEmpty(p.MainWindowTitle))
                    .Select(p =>
                    {
                        string startTimeStr = "Нет доступа";
                        try { startTimeStr = p.StartTime.ToString(); } catch { }

                        return new
                        {
                            Title = p.MainWindowTitle,
                            StartTime = startTimeStr
                        };
                    }).OrderBy(a => a.Title).ToList();

                AppListView.ItemsSource = appList;
            }
            catch { }
        }

        private void KillProcessById(int id)
        {
            try
            {
                Process process = Process.GetProcessById(id);
                process.Kill();
                process.WaitForExit();
                RefreshData();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка: {ex.Message}");
            }
        }

        private void KillTaskMenuItem_Click(object sender, RoutedEventArgs e)
        {
            if (ProcessListView.SelectedItem != null)
            {
                dynamic selectedItem = ProcessListView.SelectedItem;
                int id = selectedItem.Id;
                KillProcessById(id);
            }
            else
                MessageBox.Show("Выберите процесс.");
        }

        private void NewTaskMenuItem_Click(object sender, RoutedEventArgs e)
        {
            NewTaskWindow taskWindow = new();
            taskWindow.ShowDialog();
            RefreshData(); 
        }

        private void ExitMenuItem_Click(object sender, RoutedEventArgs e) 
            => Application.Current.Shutdown();
    }
}