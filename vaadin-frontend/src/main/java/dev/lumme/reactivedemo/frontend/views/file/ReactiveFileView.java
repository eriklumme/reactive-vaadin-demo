package dev.lumme.reactivedemo.frontend.views.file;

import com.vaadin.flow.router.PageTitle;
import dev.lumme.reactivedemo.common.client.FileClient;
import dev.lumme.reactivedemo.frontend.file.FileReader;

import java.io.File;

@PageTitle("Reactive files")
public class ReactiveFileView extends AbstractFileView {

    public ReactiveFileView(FileReader fileReader, FileClient fileClient) {
        super(fileReader, fileClient);
    }

    @Override
    protected void loadFile(File file, Runnable onCompleted) {
        fileReader.readReactive(file, onCompleted);
    }
}
