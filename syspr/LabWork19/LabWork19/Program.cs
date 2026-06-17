namespace LabWork19
{
    internal class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                return;
            }

            string targetDir = args[0];
            string mode = args[1].ToLower();

            if (!Directory.Exists(targetDir))
            {
                Console.WriteLine($"Каталог не существует.");
                return;
            }

            try
            {
                switch (mode)
                {
                    case "date":
                        SortByDate(targetDir);
                        break;
                    case "ext":
                        SortByExtension(targetDir);
                        break;
                    case "flatten":
                        FlattenDirectory(targetDir);
                        break;
                    default:
                        Console.WriteLine("Неизвестный режим.");
                        break;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        static void SortByDate(string baseDir)
        {
            string[] files = Directory.GetFiles(baseDir);

            foreach (string file in files)
            {
                //DateTime creationTime = File.GetCreationTime(file);
                DateTime creationTime = File.GetLastWriteTime(file);

                string year = creationTime.ToString("yyyy");
                string month = creationTime.ToString("MM");
                string day = creationTime.ToString("dd");

                string destDir = Path.Combine(baseDir, year, month, day);
                Directory.CreateDirectory(destDir);

                string destFile = Path.Combine(destDir, Path.GetFileName(file));
                File.Move(file, destFile);
            }
            Console.WriteLine("Сортировка завершена.");
        }

        static void SortByExtension(string baseDir)
        {
            string[] files = Directory.GetFiles(baseDir);

            foreach (string file in files)
            {
                string ext = Path.GetExtension(file).TrimStart('.').ToUpper();
                if (string.IsNullOrEmpty(ext)) 
                    ext = "UNKNOWN";

                string destDir = Path.Combine(baseDir, ext);
                Directory.CreateDirectory(destDir);

                string destFile = Path.Combine(destDir, Path.GetFileName(file));
                File.Move(file, destFile);
            }
            Console.WriteLine("Сортировка завершена.");
        }

        static void FlattenDirectory(string baseDir)
        {
            string[] allFiles = Directory.GetFiles(baseDir, "*.*", SearchOption.AllDirectories);

            foreach (string file in allFiles)
            {
                if (Path.GetDirectoryName(file) == baseDir) continue;

                string fileName = Path.GetFileNameWithoutExtension(file);
                string ext = Path.GetExtension(file);

                string destFile = Path.Combine(baseDir, fileName + ext);
                int counter = 1;

                while (File.Exists(destFile))
                {
                    destFile = Path.Combine(baseDir, $"{fileName}({counter}){ext}");
                    counter++;
                }

                File.Move(file, destFile);
            }

            DeleteEmptySubdirectories(baseDir);
            Console.WriteLine("Слияние завершено.");
        }

        static void DeleteEmptySubdirectories(string startDir)
        {
            foreach (string directory in Directory.GetDirectories(startDir))
            {
                DeleteEmptySubdirectories(directory);

                if (Directory.GetFiles(directory).Length == 0 
                    && Directory.GetDirectories(directory).Length == 0)
                    Directory.Delete(directory);
            }
        }
    }
}

