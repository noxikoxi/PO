Program zadanie1;

procedure generator(lower: integer; upper: integer; var arr: array of integer);
var i: integer;
begin
    Randomize;
    for i := 0 to Length(arr)-1 do
    begin
        arr[i] := random(upper + 1 - lower) + lower;
    end;
end;

procedure bubbleSort(var arr: array of integer; size: integer);
var
    i: integer;
    j: integer;
    temp: integer;
begin
    for i := 0 to size-1 do
    begin
        for j :=0 to size-2 do
        begin
            if arr[j] > arr[j+1] then
            begin
                temp := arr[j];
                arr[j] := arr[j+1];
                arr[j+1] := temp;
            end;
        end;
    end;
end;

procedure showTestResults(passed: boolean; name: string);
begin
    if passed then
        writeln('Test ', name, ' zakończony pomyślnie')
    else
        writeln('Test ', name, ' zakończony niepowodzeniem');

    writeln;
end;

procedure TestBubbleSortAscending;
var
    arr: array[0..9] of integer = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    i: integer;
    passed: boolean;
begin
    passed := True;
    bubbleSort(arr, Length(arr));
    for i := 0 to Length(arr)-1 do
    begin
        if arr[i] <> i+1 then
        begin
            passed := False;
            break;
        end;
    end;
    showTestResults(passed, 'TestBubbleSortAscending');
end;

procedure TestBubbleSortDescending;
var
    arr: array[0..9] of integer = (10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
    i: integer;
    passed: boolean;
begin
    passed := True;
    bubbleSort(arr, Length(arr));
    for i := 0 to Length(arr)-1 do
    begin
        if arr[i] <> i+1 then
        begin
            passed := False;
            break;
        end;
    end;
    showTestResults(passed, 'TestBubbleSortAscending');
end;

procedure TestBubbleSortRandom;
var
    arr: array[0..9] of integer = (5, 2, 7, 8, 3, 10, 1, 6, 9, 4);
    i: integer;
    passed: boolean;
begin
    bubbleSort(arr, Length(arr));
    for i := 0 to Length(arr)-1 do
    begin
        if arr[i] <> i+1 then
        begin
            passed := False;
            break;
        end;
    end;
    showTestResults(passed, 'TestBubbleSortRandom');
end;

procedure TestGeneratorRange;
var
    arr: array[0..100] of integer;
    i: integer;
    passed: boolean;
begin
    passed := true;
    generator(0, 10, arr);
    for i := 0 to Length(arr)-1 do
    begin
        if (arr[i] < 0) or (arr[i] > 10) then
        begin
            passed := False;
            break;
        end;
    end;
    showTestResults(passed, 'TestGeneratorRange');
end;

procedure TestGeneratorSize;
var
    arr: array[0..100] of integer;
    i: integer;
    passed: boolean;
begin
    passed := true;
    for i := 0 to Length(arr) do
    begin
        arr[i] := 0;
    end;

    generator(10, 100, arr);
    for i := 0 to Length(arr)-1 do
    begin
        if arr[i] = 0 then
        begin
            passed := False;
            break;
        end;
    end;

    showTestResults(passed, 'TestGeneratorSize');
end;


var
    size: integer;
    numbers: array of integer;
    i: integer;
begin
    size := 10;
    SetLength(numbers, size);

    TestBubbleSortAscending();
    TestBubbleSortDescending();
    TestBubbleSortRandom();
    TestGeneratorRange();
    TestGeneratorSize();

    generator(-10, 10, numbers);
    
    writeln('Tablica przed sortowaniem:');

    for i := 0 to size-1 do
    begin
        write(numbers[i], ' ');
    end;
    writeln;

    writeln('Tablica po sortowaniu:');
    bubbleSort(numbers, size);

    for i := 0 to size-1 do
    begin
        write(numbers[i], ' ');
    end;

    writeln;
end.
