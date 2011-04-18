package org.nuxeo.ecm.platform.wi.backend.wss;

import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.wss.WSSException;
import org.nuxeo.wss.spi.AbstractWSSListItem;
import org.nuxeo.wss.spi.WSSListItem;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Organization: Gagnavarslan ehf
 */
public class VirtualListItem extends AbstractWSSListItem implements WSSListItem {

    protected String name;
    protected String corePathPrefix;
    protected String urlRoot;

    public VirtualListItem(String name, String corePathPrefix, String urlRoot) {
        this.name = name;
        this.corePathPrefix = corePathPrefix;
        this.urlRoot=urlRoot;
    }

    @Override
    protected Date getCheckoutDate() {
        return Calendar.getInstance().getTime();
    }

    @Override
    protected Date getCheckoutExpiryDate() {
        Date to = getCheckoutDate();
        if (to != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(to);
            cal.add(Calendar.MINUTE, 20);
            return cal.getTime();
        }
        return Calendar.getInstance().getTime();
    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public String getSubPath() {
        Path completePath = new Path(urlRoot);
        completePath = completePath.append(name);
        String path = completePath.toString();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public void setDescription(String s) {
        //nothing
    }

    @Override
    public InputStream getStream() {
        return null;
    }

    @Override
    public void setStream(InputStream inputStream, String s) throws WSSException {
        //nothing
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEtag() {
        return name;
    }

    @Override
    public String getType() {
        return "folder";
    }

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public void checkOut(String s) throws WSSException {
        //nothing
    }

    @Override
    public void uncheckOut(String s) throws WSSException {
        //nothing
    }

    @Override
    public String getLastModificator() {
        return null;
    }

    @Override
    public String getCheckoutUser() {
        return "";
    }

    @Override
    public Date getCreationDate() {
        return Calendar.getInstance().getTime();
    }

    @Override
    public Date getModificationDate() {
        return Calendar.getInstance().getTime();
    }
}
