function [X, Y, Z] = shuf3(A, B, C)
ordering = randperm(size(A,1));
X = A(ordering, :);
Y = B(ordering, :);
Z = C(ordering, :);
end