package com.rsnt.common.security;

import static org.jboss.seam.ScopeType.APPLICATION;

import java.io.Serializable;
import java.util.Set;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.permission.JpaPermissionStore;
import org.jboss.seam.security.permission.PermissionResolver;
import org.jboss.seam.security.permission.PermissionStore;

@Name("org.jboss.seam.security.persistentPermissionResolver")
@Scope(APPLICATION)
@BypassInterceptors
@Install(precedence = Install.APPLICATION)
@Startup
public class RsntPermissionResolver implements PermissionResolver, Serializable {

    private static final long serialVersionUID = -6395762056529303551L;

    private PermissionStore permissionStore;

    @Create
    public void create() {
        initPermissionStore();
    }

    protected void initPermissionStore() {
        if (permissionStore == null) {
            permissionStore = (PermissionStore) Component.getInstance(JpaPermissionStore.class, true);
        }
    }

    public PermissionStore getPermissionStore() {
        return permissionStore;
    }

    public void setPermissionStore(final PermissionStore permissionStore) {
        this.permissionStore = permissionStore;
    }

    public boolean hasPermission(final Object target, final String permission) {
        final Identity identity = Identity.instance();
        if (permission == null) {
            return false;
        }
        final String perm = permission.trim();

        if (!identity.isLoggedIn()) {
            return false;
        }
        if (identity.hasRole(perm)) {
            return true;
        }
        return false;
    }

    public void filterSetByAction(final Set<Object> targets, final String action) {
        if (permissionStore == null) {
            return;
        }

        final Identity identity = Identity.instance();
        
    }
}
