package org.nuxeo.ecm.platform.wi.backend.webdav;

import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.platform.wi.backend.Backend;
import org.nuxeo.ecm.webdav.backend.WebDavBackend;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 24.02.2011
 * Time: 11:10:21
 *
 * @author Vitalii Siryi
 */
public class WebDavBackendAdapter implements WebDavBackend {

    private Backend backend;

    public WebDavBackendAdapter(Backend backend) {
        this.backend = backend;
    }

    @Override
    public void saveChanges() throws ClientException {
        backend.saveChanges();
    }

    @Override
    public void discardChanges() throws ClientException {
        backend.discardChanges();
    }

    @Override
    public boolean isLocked(DocumentRef ref) throws ClientException {
        return backend.isLocked(ref);
    }

    @Override
    public boolean canUnlock(DocumentRef ref) throws ClientException {
        return backend.canUnlock(ref);
    }

    @Override
    public String lock(DocumentRef ref) throws ClientException {
        return backend.lock(ref);
    }

    @Override
    public boolean unlock(DocumentRef ref) throws ClientException {
        return backend.unlock(ref);
    }

    @Override
    public String getCheckoutUser(DocumentRef ref) throws ClientException {
        return backend.getCheckoutUser(ref);
    }

    @Override
    public Path parseLocation(String location) {
        return backend.parseLocation(location);
    }

    @Override
    public DocumentModel resolveLocation(String location) throws ClientException {
        return backend.resolveLocation(location);
    }

    @Override
    public void removeItem(String location) throws ClientException {
        backend.removeItem(location);
    }

    @Override
    public void removeItem(DocumentRef ref) throws ClientException {
        backend.removeItem(ref);
    }

    @Override
    public void renameItem(DocumentModel source, String destinationName) throws ClientException {
        backend.renameItem(source, destinationName);
    }

    @Override
    public DocumentModel moveItem(DocumentModel source, PathRef targetParentRef) throws ClientException {
        return backend.moveItem(source, targetParentRef);
    }

    @Override
    public DocumentModel copyItem(DocumentModel source, PathRef targetParentRef) throws ClientException {
        return backend.copyItem(source, targetParentRef);
    }

    @Override
    public DocumentModel createFolder(String parentPath, String name) throws ClientException {
        return backend.createFolder(parentPath, name);
    }

    @Override
    public DocumentModel createFile(String parentPath, String name, Blob content) throws ClientException {
        return backend.createFile(parentPath, name, content);
    }

    @Override
    public DocumentModel createFile(String parentPath, String name) throws ClientException {
        return backend.createFile(parentPath, name);
    }

    @Override
    public DocumentModel saveDocument(DocumentModel documentModel) throws ClientException {
        return backend.saveDocument(documentModel);
    }

    @Override
    public List<DocumentModel> getChildren(DocumentRef ref) throws ClientException {
        return backend.getChildren(ref);
    }

    @Override
    public boolean isRename(String source, String destination) {
        return backend.isRename(source, destination);
    }

    @Override
    public boolean exists(String location) {
        return backend.exists(location);
    }

    @Override
    public boolean hasPermission(DocumentRef documentRef, String s) throws ClientException {
        return backend.hasPermission(documentRef, s);
    }

    @Override
    public String getDisplayName(DocumentModel doc) {
        return backend.getDisplayName(doc);
    }

    @Override
    public LinkedList<String> getVirtualFolderNames() throws ClientException {
        return backend.getVirtualFolderNames();
    }

    @Override
    public boolean isVirtual() {
        return backend.isVirtual();
    }
}
