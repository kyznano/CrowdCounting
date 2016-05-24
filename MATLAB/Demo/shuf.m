function C = shuf(A)
ordering = randperm(size(A,1));
C = A(ordering, :);
end