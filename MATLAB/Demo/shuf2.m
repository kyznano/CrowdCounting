function [C, D] = shuf2(A, B)
ordering = randperm(size(A,1));
C = A(ordering, :);
D = B(ordering, :);
end