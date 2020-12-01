package dev.lumme.reactivedemo.frontend.views.file;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.lumme.reactivedemo.common.client.FileClient;
import dev.lumme.reactivedemo.common.dto.FileDTO;
import dev.lumme.reactivedemo.frontend.file.FileReader;
import dev.lumme.reactivedemo.frontend.views.main.MainView;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

@Route(layout = MainView.class)
public abstract class AbstractFileView extends VerticalLayout {

    protected final FileReader fileReader;

    private final FileClient fileClient;

    public AbstractFileView(FileReader fileReader, FileClient fileClient) {
        this.fileReader = fileReader;
        this.fileClient = fileClient;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (attachEvent.isInitialAttach()) {
            init();
        }
    }

    private void init() {
//        Grid<FileDTO> files = new Grid<>();
//        files.addColumn(FileDTO::getFilename).setHeader("Filename");
//        files.addComponentColumn(file -> {
//            Span span = new Span();
//            span.add(VaadinIcon.CLIPBOARD_CROSS.create());
//            loadFile(getFile(file), () -> setLoadedIcon(span));
//            return span;
//        }).setHeader("Status");
//
//        files.setDataProvider(DataProvider.fromCallbacks(
//                query -> fileClient.getFiles(query.getOffset(), query.getLimit()).stream(),
//                query -> fileClient.countFiles()
//        ));
//
//        add(files);

        int numFiles = 30;
        File file;
        try {
            file = ResourceUtils.getFile(getClass().getResource("/testfile.foo"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        AtomicInteger counter = new AtomicInteger(numFiles);

        long startTime = System.nanoTime();
        Runnable onCompleted = () -> {
            if (counter.decrementAndGet() == 0) {
                long endTime = System.nanoTime();
                System.out.printf("Finished %d files in %.2f ms%n", numFiles, (endTime - startTime) / 1000000.0);
            }
        };
        for (int i = 0; i < numFiles; i++) {
            loadFile(file, onCompleted);
        }
    }

    private File getFile(FileDTO fileDTO) {
        try {
            return ResourceUtils.getFile(getClass().getResource("/" + fileDTO.getPath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file", e);
        }
    }

    private void setLoadedIcon(Span span) {
        getUI().ifPresent(ui -> {
            ui.access(() -> {
                span.removeAll();
                span.add(VaadinIcon.CLIPBOARD_CHECK.create());
            });
        });
    }

    protected abstract void loadFile(File file, Runnable onCompleted);
}
