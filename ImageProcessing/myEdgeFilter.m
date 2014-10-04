function [Im Io Ix Iy] = myEdgeFilter(img, sigma)
hsize = [3 3];

if(ischar(img))
    img = imread(img);
end
if(size(img,3)==3)
    img = rgb2gray(img);
end
image = im2double(img);
%image = im2double(imread(img));
gx=[-1 0 1;-2 0 2;-1 0 1];
gy=[1 2 1;0 0 0;-1 -2 -1];


h = fspecial('gaussian', hsize, sigma);
smoothImage = myImageFilter(image,h);
Ix = abs(myImageFilter(smoothImage,gx));
IxMax = max(max(Ix));
IxMin = min(min(Ix));

for i=1:size(Ix,1)
    for j=1:size(Ix,2)
        Ix(i,j) = (Ix(i,j)-IxMin)/(IxMax-IxMin);
    end
end

IxCopy = Ix;
%imshow(Ix);
%pause;

for i=2:size(Ix,1)-1
    for j=2:size(Ix,2)-1
        
            if(Ix(i,j-1)>=Ix(i,j) || Ix(i,j+1)>=Ix(i,j))
                IxCopy(i,j) = 0;
            end
        
    end
end



%imshow(Ix);
%hold on;

Iy = abs(myImageFilter(smoothImage,gy));

IyMax = max(max(Iy));
IyMin = min(min(Iy));

for i=1:size(Iy,1)
    for j=1:size(Iy,2)
        Iy(i,j) = (Iy(i,j)-IyMin)/(IyMax-IyMin);
    end
end

IyCopy = Iy;
%imshow(Iy);
%pause;
for i=2:size(Iy,1)-1
    for j=2:size(Iy,2)-1
        
            if(Iy(i-1,j)>=Iy(i,j) || Iy(i+1,j)>=Iy(i,j))
                IyCopy(i,j) = 0;
            end
        
    end
end
%imshow(Iy);
Ix=IxCopy;
Iy=IyCopy;
Im = sqrt((Ix.^2 + Iy.^2));
Io = atan(Iy./Ix);
