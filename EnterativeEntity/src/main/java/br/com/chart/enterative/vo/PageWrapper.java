package br.com.chart.enterative.vo;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 * @param <T>
 */
public class PageWrapper<T> {

    public static final int MAX_PAGE_ITEM_DISPLAY = 5;
    private final Page<T> page;
    @Getter private List<PageItem> items;
    @Getter private final int number;
    @Getter @Setter private String url;

    public PageWrapper(Page<T> page, String url) {
        this.page = page;
        this.url = url;
        this.items = new ArrayList<>();

        this.number = page.getNumber() + 1; //start from 1 to match page.page

        int start, size;
        if (page.getTotalPages() <= MAX_PAGE_ITEM_DISPLAY) {
            start = 1;
            size = page.getTotalPages();
        } else if (this.number <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY / 2) {
            start = 1;
            size = MAX_PAGE_ITEM_DISPLAY;
        } else if (this.number >= page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY / 2) {
            start = page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY + 1;
            size = MAX_PAGE_ITEM_DISPLAY;
        } else {
            start = this.number - MAX_PAGE_ITEM_DISPLAY / 2;
            size = MAX_PAGE_ITEM_DISPLAY;
        }

        for (int i = 0; i < size; i++) {
            this.items.add(new PageItem(start + i, (start + i) == this.number));
        }
    }

    public long getTotalElements() {
        return page.getTotalElements();
    }

    public List<T> getContent() {
        return page.getContent();
    }

    public int getSize() {
        return page.getSize();
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public boolean isFirstPage() {
        return page.isFirst();
    }

    public boolean isLastPage() {
        return page.isLast();
    }

    public boolean isHasPreviousPage() {
        return page.hasPrevious();
    }

    public boolean isHasNextPage() {
        return page.hasNext();
    }

    public class PageItem {

        @Getter private final int number;
        @Getter private final boolean current;

        public PageItem(int number, boolean current) {
            this.number = number;
            this.current = current;
        }
    }
}
