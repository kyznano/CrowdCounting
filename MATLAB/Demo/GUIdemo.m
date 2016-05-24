function varargout = GUIdemo(varargin)
% GUIDEMO MATLAB code for GUIdemo.fig
%      GUIDEMO, by itself, creates a new GUIDEMO or raises the existing
%      singleton*.
%
%      H = GUIDEMO returns the handle to a new GUIDEMO or the handle to
%      the existing singleton*.
%
%      GUIDEMO('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in GUIDEMO.M with the given input arguments.
%
%      GUIDEMO('Property','Value',...) creates a new GUIDEMO or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before GUIdemo_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to GUIdemo_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help GUIdemo

% Last Modified by GUIDE v2.5 03-Aug-2014 15:17:05

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @GUIdemo_OpeningFcn, ...
                   'gui_OutputFcn',  @GUIdemo_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before GUIdemo is made visible.
function GUIdemo_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to GUIdemo (see VARARGIN)

% Choose default command line output for GUIdemo
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);
axes(handles.image);
imshow('null.png')

global XAll;
global YAll;
global allImagePath;
global onefold;
global mlmodel;
global numimage;

numimage = 0;

% UIWAIT makes GUIdemo wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = GUIdemo_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;



function edittext_nimage_Callback(hObject, eventdata, handles)
% hObject    handle to edittext_nimage (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edittext_nimage as text
%        str2double(get(hObject,'String')) returns contents of edittext_nimage as a double


% --- Executes during object creation, after setting all properties.
function edittext_nimage_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edittext_nimage (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in radio_test.
function radio_test_Callback(hObject, eventdata, handles)
% hObject    handle to radio_test (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of radio_test


% --- Executes on button press in radio_dataset.
function radio_dataset_Callback(hObject, eventdata, handles)
% hObject    handle to radio_dataset (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of radio_dataset

% --- Executes on button press in checkbox_runestimating.
function checkbox_runestimating_Callback(hObject, eventdata, handles)
% hObject    handle to checkbox_runestimating (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of checkbox_runestimating



function edittext_kfold_Callback(hObject, eventdata, handles)
% hObject    handle to edittext_kfold (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edittext_kfold as text
%        str2double(get(hObject,'String')) returns contents of edittext_kfold as a double


% --- Executes during object creation, after setting all properties.
function edittext_kfold_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edittext_kfold (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edittext_gridrow_Callback(hObject, eventdata, handles)
% hObject    handle to edittext_gridrow (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edittext_gridrow as text
%        str2double(get(hObject,'String')) returns contents of edittext_gridrow as a double


% --- Executes during object creation, after setting all properties.
function edittext_gridrow_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edittext_gridrow (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edittext_gridcolumn_Callback(hObject, eventdata, handles)
% hObject    handle to edittext_gridcolumn (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edittext_gridcolumn as text
%        str2double(get(hObject,'String')) returns contents of edittext_gridcolumn as a double


% --- Executes during object creation, after setting all properties.
function edittext_gridcolumn_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edittext_gridcolumn (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on selection change in listbox1.
function listbox1_Callback(hObject, eventdata, handles)
% hObject    handle to listbox1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns listbox1 contents as cell array
%        contents{get(hObject,'Value')} returns selected item from listbox1


% --- Executes during object creation, after setting all properties.
function listbox1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to listbox1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: listbox controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in button_run.
function button_run_Callback(hObject, eventdata, handles)
% hObject    handle to button_run (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
    
    global XAll;
    global YAll;
    global YAllRaw;
    global allImagePath;
    global allImagePathRaw;
    global onefold;
    global mlmodel;
    global numimage;
    
    set(hObject, 'Enable', 'off');

    TEST = 1;
    TRAIN = 0;        

    IND = get(handles.textview_ind,'String');
    if(IND == '_')
        IND = 1;
    else
%       TODO
        IND = 1;
    end        
    
%     if(get(handles.checkbox_runestimating,'Value')==get(handles.checkbox_runestimating,'Max'))
%         RUN_ESTIMATE = true;
%     else
%         RUN_ESTIMATE = false;
%     end
    
    if(get(handles.radio_dataset,'Value')==get(handles.radio_dataset,'Max'))
        TYPE_DEMO = TRAIN;
    else
        TYPE_DEMO = TEST;
    end
    
    KFOLD = str2num(get(handles.edittext_kfold,'String'));
    GRID_SIZE = [str2num(get(handles.edittext_gridrow,'String')) str2num(get(handles.edittext_gridcolumn,'String'))];
    %   TODO DATASET
    DEMO_DATASET = get(handles.popup_dataset,'Value');
    DS_NAME = cell(1,1);
    Dname = cell(12,1);
    Dname{1} = 'S1L1_1_V1';
    Dname{2} = 'S1L1_1_V2';
    Dname{3} = 'S1L1_1_V3';
    Dname{4} = 'S1L1_2_V1';
    Dname{5} = 'S1L1_2_V2';
    Dname{6} = 'S1L1_2_V3';
    Dname{7} = 'S1L2_1_V1';
    Dname{8} = 'S1L2_1_V2';
    Dname{9} = 'S1L2_1_V3';
    Dname{10} = 'S1L3_1_V1';
    Dname{11} = 'S1L3_1_V2';
    Dname{12} = 'S1L3_1_V3';    
    DS_NAME{1} = Dname{DEMO_DATASET};
    GT = load('../ground_truth/PETS2009_ground_truth');
    Content = cell(12,1);
    Content{1} = GT.S1L1_1_V1;
    Content{2} = GT.S1L1_1_V2;
    Content{3} = GT.S1L1_1_V3;
    Content{4} = GT.S1L1_2_V1;
    Content{5} = GT.S1L1_2_V2;
    Content{6} = GT.S1L1_2_V3;
    Content{7} = GT.S1L2_1_V1;
    Content{8} = GT.S1L2_1_V2;
    Content{9} = GT.S1L2_1_V3;
    Content{10} = GT.S1L3_1_V1;
    Content{11} = GT.S1L3_1_V2;
    Content{12} = GT.S1L3_1_V3;
    DATASET_GROUNDTRUTH = cell(1,1);
    DATASET_GROUNDTRUTH{1} = Content{DEMO_DATASET};
    
    
%     if RUN_ESTIMATE
        set(handles.textview_status,'String','Program is running, please wait...');        
        run('estimate.m');
%     else
%         load(strcat('result/demo_version_new/',DS_NAME{1}));
%     end    
    
    
    allImagePath = Input_Img;
    allImagePathRaw = Input_Img_Raw;
    YAllRaw = Y_All;
    YAll = Y_All_S;
    XAll = X_All_S;
    onefold = one_fold;
    mlmodel = model;
    numimage = number_images;
    
    path = allImagePath{IND};
    currentGt = YAll(IND,2);
    testFeature = XAll(IND,:);
    FOLD = floor((IND-1)/onefold)+1;
    yresult = simlssvm(mlmodel{FOLD}, testFeature);
    
    set(handles.textview_ind,'String',num2str(IND));
    axes(handles.image);
    imshow(path)
    set(handles.textview_result,'String',num2str(uint16(yresult)));
    set(handles.textview_groundtruth,'String',num2str(currentGt));
    
    set(handles.textview_MAE,'String',num2str(round(statistic(1,1)*100)/100));
    set(handles.textview_MRE,'String',strcat(num2str(round(statistic(1,4)*100)/100),' %'));
    
    set(handles.textview_accuracy,'String',strcat(num2str(round(accuracy*100)/100),' %'));
    
    
    set(hObject, 'Enable', 'on');
    set(handles.textview_status,'String','');
    set(hObject,'String','RUN NOW');  
    
% --- Executes on button press in button_previous.
function button_previous_Callback(hObject, eventdata, handles)
% hObject    handle to button_previous (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

    global XAll;
    global YAll;
    global YAllRaw;
    global allImagePath;
    global allImagePathRaw;
    global onefold;
    global mlmodel;
    global numimage;
    
    set(hObject, 'Enable', 'off');
    IND = get(handles.textview_ind,'String');
    if(IND == '_')
    else
        IND = str2num(IND);
        if(IND>1)
            IND = IND-1;
            if(get(handles.radio_test,'Value')==get(handles.radio_test,'Max'))                
                path = allImagePath{IND};
                currentGt = YAll(IND,2);
                testFeature = XAll(IND,:);
                FOLD = floor((IND-1)/onefold)+1;
                yresult = simlssvm(mlmodel{FOLD}, testFeature);                
                set(handles.textview_result,'String',num2str(uint16(yresult)));                 
            elseif(get(handles.radio_dataset,'Value')==get(handles.radio_dataset,'Max'))
                path = allImagePathRaw{IND};
                currentGt = YAllRaw(IND,2);
                set(handles.textview_result,'String','_');
            end
            set(handles.textview_ind,'String',num2str(IND)); 
            axes(handles.image);
            imshow(path)
            set(handles.textview_groundtruth,'String',num2str(currentGt)); 
        end
    end   
    set(hObject, 'Enable', 'on');

% --- Executes on button press in button_next.
function button_next_Callback(hObject, eventdata, handles)
% hObject    handle to button_next (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

    global XAll;
    global YAll;
    global YAllRaw;
    global allImagePath;
    global allImagePathRaw;
    global onefold;
    global mlmodel;
    global numimage;
    
    set(hObject, 'Enable', 'off');
    IND = get(handles.textview_ind,'String');
    if(IND == '_')
    else
        IND = str2num(IND);
        if(IND<numimage)
            IND = IND+1;
            if(get(handles.radio_test,'Value')==get(handles.radio_test,'Max'))                
                path = allImagePath{IND};
                currentGt = YAll(IND,2);
                testFeature = XAll(IND,:);
                FOLD = floor((IND-1)/onefold)+1;
                yresult = simlssvm(mlmodel{FOLD}, testFeature);                
                set(handles.textview_result,'String',num2str(uint16(yresult)));                 
            elseif(get(handles.radio_dataset,'Value')==get(handles.radio_dataset,'Max'))
                path = allImagePathRaw{IND};
                currentGt = YAllRaw(IND,2);
                set(handles.textview_result,'String','_');
            end
            set(handles.textview_ind,'String',num2str(IND)); 
            axes(handles.image);
            imshow(path)
            set(handles.textview_groundtruth,'String',num2str(currentGt)); 
        end
    end   
    set(hObject, 'Enable', 'on');

% --- Executes on selection change in popup_dataset.
function popup_dataset_Callback(hObject, eventdata, handles)
% hObject    handle to popup_dataset (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns popup_dataset contents as cell array
%        contents{get(hObject,'Value')} returns selected item from popup_dataset


% --- Executes during object creation, after setting all properties.
function popup_dataset_CreateFcn(hObject, eventdata, handles)
% hObject    handle to popup_dataset (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
