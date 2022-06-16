package de.hhn.aib.labsw.blackmirror.model.ApiDataModels;

import java.util.ArrayList;

/**
 * @author Philipp Herda
 * @version 2022-06-08
 */
public record LayoutData(ArrayList<PageData> pages, int currentPageIndex) {
}

