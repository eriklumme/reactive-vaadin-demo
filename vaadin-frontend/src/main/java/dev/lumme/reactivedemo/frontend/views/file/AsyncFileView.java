package dev.lumme.reactivedemo.frontend.views.file;

import com.vaadin.flow.router.PageTitle;
import dev.lumme.reactivedemo.common.client.FileClient;
import dev.lumme.reactivedemo.frontend.file.FileReader;

import java.io.File;

@PageTitle("Asynchronous files")
public class AsyncFileView extends AbstractFileView {

    public AsyncFileView(FileReader fileReader, FileClient fileClient) {
        super(fileReader, fileClient);
    }

    @Override
    protected void loadFile(File file, Runnable onCompleted) {
        fileReader.readAsync(file, onCompleted);
    }
}
