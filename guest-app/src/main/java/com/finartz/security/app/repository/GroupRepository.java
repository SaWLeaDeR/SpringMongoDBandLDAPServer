package com.finartz.security.app.repository;

import com.finartz.security.app.domain.AdminUser;
import com.finartz.security.app.domain.Group;
import com.finartz.security.app.domain.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class GroupRepository implements BaseLdapNameAware {

    private final static String UNIQUEMEMBER = "uniqueMember";

    @Autowired
    private LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public List<Group> findAll(){
        return ldapTemplate.search(
                query().where("objectclass").is("groupOfUniqueNames"),
                new GroupContextMapper());
    }

    public void addMemberToGroup(String groupName, AdminUser p) {
        Name groupDn = buildGroupDn(groupName);
        Name personDn = buildPersonDn(p);

        DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
        ctx.addAttributeValue(UNIQUEMEMBER, personDn);

        ldapTemplate.modifyAttributes(ctx);
    }

    public void addMemberToGroup(String groupName, Guest p) {
        Name groupDn = buildGroupDn(groupName);
        Name personDn = buildPersonDn(p);

        DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
        ctx.addAttributeValue(UNIQUEMEMBER, personDn);

        ldapTemplate.modifyAttributes(ctx);
    }

    public void removeMemberFromGroup(String groupName, AdminUser p) {
        Name groupDn = buildGroupDn(groupName);
        Name personDn = buildPersonDn(p);

        DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
        ctx.removeAttributeValue(UNIQUEMEMBER, personDn);

        ldapTemplate.modifyAttributes(ctx);
    }

    public void removeMemberFromGroup(String groupName, Guest p) {
        Name groupDn = buildGroupDn(groupName);
        Name personDn = buildPersonDn(p);

        DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
        ctx.removeAttributeValue(UNIQUEMEMBER, personDn);

        ldapTemplate.modifyAttributes(ctx);
    }

    private Name buildGroupDn(String groupName) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "groups")
                .add("cn", groupName)
                .build();
    }

    private Name buildPersonDn(AdminUser person) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", person.getId())
                .build();
    }

    private Name buildPersonDn(Guest person) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", person.getId())
                .build();
    }

    private static class GroupContextMapper extends AbstractContextMapper<Group> {
        public Group doMapFromContext(DirContextOperations context) {
            Group group = new Group();
            group.setName(context.getStringAttribute("cn"));
            Object[] members = context.getObjectAttributes(UNIQUEMEMBER);
            for (Object member : members){
                Name memberDn = LdapNameBuilder.newInstance(String.valueOf(member)).build();
                group.addMember(memberDn);
            }
            return group;
        }
    }
}