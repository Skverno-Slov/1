using System.Drawing;

object locker = new();


Console.OutputEncoding = System.Text.Encoding.UTF8;

Console.Write("Введите путь к папке с изображениями: ");
string inputFolder = Console.ReadLine();

Console.Write("Введите путь к папке для сохранения: ");
string outputFolder = Console.ReadLine();

if (!Directory.Exists(inputFolder))
{
    Console.WriteLine("Ошибка: папка c картинуками не существует.");
    return;
}

if (!Directory.Exists(outputFolder))
{
    Directory.CreateDirectory(outputFolder);
}

string[] extensions = { ".jpg", ".jpeg", ".png", ".bmp" };
string[] filePaths = Directory.GetFiles(inputFolder)
    .Where(file => extensions.Contains(Path.GetExtension(file).ToLower()))
    .ToArray();

int totalFiles = filePaths.Length;

if (totalFiles == 0)
{
    Console.WriteLine("В папке не найдено изображений.");
    return;
}

int processedFilesCount = 0;

DrawProgressBar(0, totalFiles);

Parallel.ForEach(filePaths, filePath =>
{
    try
    {
        using (Bitmap bitmap = new Bitmap(filePath))
        {
            for (int y = 0; y < bitmap.Height; y++)
            {
                for (int x = 0; x < bitmap.Width; x++)
                {
                    Color originalColor = bitmap.GetPixel(x, y);
                    Color invertedColor = Color.FromArgb(
                        originalColor.A,
                        255 - originalColor.R,
                        255 - originalColor.G,
                        255 - originalColor.B
                    );
                    bitmap.SetPixel(x, y, invertedColor);
                }
            }

            string fileName = Path.GetFileName(filePath);
            string outputPath = Path.Combine(outputFolder, fileName);
            bitmap.Save(outputPath);
        }
    }
    catch (Exception ex)
    {
        lock (locker)
        {
            Console.Write(new string(' ', Console.WindowWidth - 1) + "\r");
            Console.WriteLine($"Ошибка {Path.GetFileName(filePath)}: {ex.Message}");
        }
    }
    finally
    {
        int currentProcessed = Interlocked.Increment(ref processedFilesCount);

        lock (locker)
        {
            DrawProgressBar(currentProcessed, totalFiles);
        }
    }
});

Console.WriteLine("\nОбработка успешно завершена");
Console.ReadKey();

void DrawProgressBar(int current, int total)
{
    double percentage = (double)current / total * 100;
    int progressWidth = 30;
    int filledWidth = (int)(percentage / 100 * progressWidth);

    string bar = new string('#', filledWidth) + new string('-', progressWidth - filledWidth);

    Console.Write($"\r[{bar}] {percentage:F1}%");
}
