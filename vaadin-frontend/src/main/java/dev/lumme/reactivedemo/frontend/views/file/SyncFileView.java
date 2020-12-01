package dev.lumme.reactivedemo.frontend.views.file;

import com.vaadin.flow.router.PageTitle;
import dev.lumme.reactivedemo.common.client.FileClient;
import dev.lumme.reactivedemo.frontend.file.FileReader;

import java.io.File;

@PageTitle("Synchronous files")
public class SyncFileView extends AbstractFileView {

    public SyncFileView(FileReader fileReader, FileClient fileClient) {
        super(fileReader, fileClient);
    }

    @Override
    protected void loadFile(File file, Runnable onCompleted) {
        fileReader.readSync(file, onCompleted);
    }

}
