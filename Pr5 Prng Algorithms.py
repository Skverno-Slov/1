"""
Практическая работа №5: Алгоритмы получения псевдослучайных чисел.

Содержит реализацию четырех задач:
1. Алгоритм середины квадратов Неймана.
2. Модифицированный метод Неймана.
3. Алгоритм Лемера (метод дробных частей).
4. Мультипликативный конгруэнтный метод Лемера.
"""

from decimal import Decimal


def extract_middle_4_digits(product: Decimal) -> tuple[Decimal, str]:
    """
    Извлекает 4 средние цифры из 8-значной дробной части числа product (в диапазоне [0, 1)).
    Возвращает кортеж: (новое 4-значное число Decimal, строка 8 знаков после запятой).
    """
    # Форматируем число с точностью до 8 знаков после запятой
    formatted = f"{product:.8f}"
    frac_digits = formatted.split(".")[1]  # 8 цифр после запятой
    middle_4 = frac_digits[2:6]            # средние 4 цифры (индексы 2, 3, 4, 5)
    return Decimal("0." + middle_4), frac_digits


def task_1_neumann_middle_squares(r0: str = "0.5836", n: int = 6) -> list[Decimal]:
    """
    Задача 1.
    Сгенерировать базовую последовательность N случайных чисел
    методом Неймана (срединных квадратов). N=6, R0=0.5836.
    """
    print("=" * 72)
    print("ЗАДАЧА 1. Метод середины квадратов Неймана")
    print(f"Исходные данные: N = {n}, R0 = {r0}")
    print("-" * 72)

    r_current = Decimal(r0)
    sequence = [r_current]
    print(f"R0 = {r_current:.4f}")

    # Генерируем последующие числа (R1 .. R_{n-1}, а также R_n для полноты)
    for i in range(n):
        r_sq = r_current * r_current
        r_next, frac_str = extract_middle_4_digits(r_sq)
        highlighted = f"0.{frac_str[:2]}[{frac_str[2:6]}]{frac_str[6:]}"
        print(f"R{i}^2 = {r_sq:.8f} ({highlighted})  ->  R{i + 1} = {r_next:.4f}")
        sequence.append(r_next)
        r_current = r_next

    print(f"\nБазовая последовательность (R0..R{n - 1}): {[float(x) for x in sequence[:n]]}")
    print(f"Сгенерированные числа (R1..R{n}):      {[float(x) for x in sequence[1:]]}")
    print()
    return sequence


def task_2_modified_neumann(
    r0: str = "0.5836", r1: str = "0.2176", n: int = 6
) -> list[Decimal]:
    """
    Задача 2.
    Сгенерировать базовую последовательность N случайных чисел
    модифицированным методом Неймана. N=6, R0=0.5836, R1=0.2176.
    """
    print("=" * 72)
    print("ЗАДАЧА 2. Модифицированный метод Неймана")
    print(f"Исходные данные: N = {n}, R0 = {r0}, R1 = {r1}")
    print("-" * 72)

    r_prev = Decimal(r0)
    r_curr = Decimal(r1)
    sequence = [r_prev, r_curr]

    for i in range(n):
        product = r_prev * r_curr
        r_next, frac_str = extract_middle_4_digits(product)
        highlighted = f"0.{frac_str[:2]}[{frac_str[2:6]}]{frac_str[6:]}"
        print(
            f"R{i}*R{i + 1} = {r_prev:.4f} * {r_curr:.4f} = {product:.8f} ({highlighted})"
            f"  ->  R{i + 2} = {r_next:.4f}"
        )
        sequence.append(r_next)
        r_prev, r_curr = r_curr, r_next

    print(f"\nСгенерированные числа (R2..R{n + 1}): {[float(x) for x in sequence[2:]]}")
    print()
    return sequence[2:]


def task_3_lehmer_fractional(
    r0: str = "0.585", g: int = 927, n: int = 5
) -> list[Decimal]:
    """
    Задача 3.
    Сгенерировать базовую последовательность N случайных чисел
    при помощи алгоритма Лемера y = {g * x}. N=5, R0=0.585, g=927.
    """
    print("=" * 72)
    print("ЗАДАЧА 3. Алгоритм Лемера y = {g * x}")
    print(f"Исходные данные: N = {n}, R0 = {r0}, g = {g}")
    print("-" * 72)

    r_curr = Decimal(r0)
    g_dec = Decimal(g)
    sequence = []

    for i in range(n):
        product = g_dec * r_curr
        # Дробная часть числа {g * R_i}
        r_next = product - int(product)
        print(
            f"g*R{i} = {g} * {r_curr:.3f} = {product:.3f},"
            f"   R{i + 1} = {{g*R{i}}} = {r_next:.3f}"
        )
        sequence.append(r_next)
        r_curr = r_next

    print(f"\nСгенерированная последовательность (R1..R{n}): {[float(x) for x in sequence]}")
    print()
    return sequence


def task_4_multiplicative_congruential(
    a: int = 265, m: int = 129, x0: int = 122, n: int = 5
) -> tuple[list[int], list[float]]:
    """
    Задача 4.
    Мультипликативным конгруэнтным методом сгенерировать базовую
    последовательность. a=265, m=129, X0=122 (N=5).
    """
    print("=" * 72)
    print("ЗАДАЧА 4. Мультипликативный конгруэнтный метод Лемера")
    print(f"Исходные данные: a = {a}, m = {m}, X0 = {x0}, N = {n}")
    print("-" * 72)

    x_curr = x0
    x_sequence = []
    r_sequence = []

    for i in range(n):
        product = a * x_curr
        quotient, remainder = divmod(product, m)
        x_next = remainder
        r_next = x_next / m
        print(
            f"a*X{i}/m = {a}*{x_curr}/{m} = {product}/{m} = {quotient}*{m} + {remainder},"
            f"   X{i + 1} = {x_next}"
        )
        x_sequence.append(x_next)
        r_sequence.append(r_next)
        x_curr = x_next

    print("\nБазовая последовательность R_i = X_i / m:")
    for i, (x_val, r_val) in enumerate(zip(x_sequence, r_sequence), start=1):
        print(f"R{i} = {x_val}/{m} = {r_val:.3f}")

    print()
    return x_sequence, r_sequence


def main() -> None:
    task_1_neumann_middle_squares(r0="0.5836", n=6)
    task_2_modified_neumann(r0="0.5836", r1="0.2176", n=6)
    task_3_lehmer_fractional(r0="0.585", g=927, n=5)
    task_4_multiplicative_congruential(a=265, m=129, x0=122, n=5)


if __name__ == "__main__":
    main()