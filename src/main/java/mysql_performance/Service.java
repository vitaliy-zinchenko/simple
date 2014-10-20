package mysql_performance;

import mysql_performance.entity.Permission;
import mysql_performance.entity.Role;
import mysql_performance.entity.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by zinchenko on 18.09.14.
 */
//@org.springframework.stereotype.Service
public class Service {


    private static final int ADMINS_NUMBER = 2;
    private static final int USERS_NUMBER = 5;
    private static final int ITERATIONS = 1;

    @Autowired
    private SessionFactory sessionFactory;

    private static List<String> roleNames = new ArrayList<String>();
    static {
        roleNames.add("admin");
        roleNames.add("user");
    }

    private static List<String> permissionsKeys = new ArrayList<String>();
    static {
        permissionsKeys.add("resource.read");
        permissionsKeys.add("resource.crate");
        permissionsKeys.add("resource.update");
        permissionsKeys.add("resource.delete");

        permissionsKeys.add("user.read");
        permissionsKeys.add("user.create");
        permissionsKeys.add("user.update");
        permissionsKeys.add("user.delete");
    }

    private static List<String> customPermissionsKeys = new ArrayList<String>();
    static {
        customPermissionsKeys.add("custom.read");
        customPermissionsKeys.add("custom.deal");
        customPermissionsKeys.add("custom.show");
        customPermissionsKeys.add("custom.send");
        customPermissionsKeys.add("custom.pull");
        customPermissionsKeys.add("custom.push");
        customPermissionsKeys.add("custom.close");
    }

    private static List<String> goodsTypeNames = new ArrayList<String>();
    static {
        goodsTypeNames.add("house");
        goodsTypeNames.add("work");
        goodsTypeNames.add("school");
        goodsTypeNames.add("computer");
        goodsTypeNames.add("car");
        goodsTypeNames.add("office");
    }

    List<Permission> permissions;
    List<Permission> customPermissions;
    List<Role> roles;
    List<User> admins;
    List<User> users;

    private void createLists(){
        permissions = new ArrayList<Permission>();
        customPermissions = new ArrayList<Permission>();
        roles = new ArrayList<Role>();
        admins = new ArrayList<User>();
        users = new ArrayList<User>();
    }

    @Transactional
    public void fill() {
        createLists();
        for (int i = 0; i < ITERATIONS; i++) {
            create();
            createLists();
            sessionFactory.getCurrentSession().clear();
            System.out.println("i = " + i);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void create(){
        createPermissions(permissionsKeys, permissions);
        createPermissions(customPermissionsKeys, customPermissions);
        createRoles();
//        createAdmins();
//        createUsers();
    }

    private void createPermissions(List<String> permissionsKeys, List<Permission> permissions) {
        for (String permKey: permissionsKeys) {
            Permission permission = (Permission) getByField(Permission.class, "key", permKey);
            if (permission != null) {
                permissions.add(permission);
                continue;
            }
            permission = new Permission();
            permission.setKey(permKey);
            save(permission);
            permissions.add(permission);
        }
    }

    private void createRoles() {

        Role role = (Role) getByField(Role.class, "name", roleNames.get(0));
        if (role == null) {
            role = new Role();
            role.setName(roleNames.get(0));
//            role.getPermissions().add(permissions.get(0));
//            role.getPermissions().add(permissions.get(1));
//            role.getPermissions().add(permissions.get(2));
//            role.getPermissions().add(permissions.get(3));
//            role.getPermissions().add(permissions.get(4));
//            role.getPermissions().add(permissions.get(5));
//            role.getPermissions().add(permissions.get(6));
//            role.getPermissions().add(permissions.get(7));
            Permission permission = (Permission) sessionFactory.getCurrentSession().get(Permission.class, 1L);
            role.getPermissions().add(permission);
            sessionFactory.getCurrentSession().save(role);
        }
        roles.add(role);

        role = (Role) getByField(Role.class, "name", roleNames.get(1));
        if (role == null) {
            role = new Role();
            role.setName(roleNames.get(1));
            role.getPermissions().add(permissions.get(0));
            role.getPermissions().add(permissions.get(4));
            sessionFactory.getCurrentSession().save(role);
        }
        roles.add(role);

    }

    private void createGoodsType() {
        for (String goodsTypeName: goodsTypeNames) {
            Permission permission = (Permission) getByField(Permission.class, "key", goodsTypeName);
            if (permission != null) {
                permissions.add(permission);
                continue;
            }
            permission = new Permission();
            permission.setKey(goodsTypeName);
            save(permission);
            permissions.add(permission);
        }
    }

    private void createAdmins() {
        for (int i = 0; i < ADMINS_NUMBER; i++) {
            User user = new User();
            user.setName("un-" + System.currentTimeMillis());
            user.setRole(roles.get(0));
            user.getPermissions().add((Permission) getRandomObject(permissions));
            user.getPermissions().add((Permission) getRandomObject(permissions));
            user.getPermissions().add((Permission) getRandomObject(permissions));
            user.getPermissions().add((Permission) getRandomObject(permissions));
            save(user);
            admins.add(user);
        }
    }

    private void createUsers() {
        for (int i = 0; i < USERS_NUMBER; i++) {
            User user = new User();
            user.setName("un-" + System.currentTimeMillis());
            user.setRole(roles.get(1));
            user.getPermissions().add((Permission) getRandomObject(customPermissions));
            user.getPermissions().add((Permission) getRandomObject(customPermissions));
            save(user);
            users.add(user);
        }
    }

    private void createGoods() {

    }

    private Object getRandomObject(List collection) {
        int size = collection.size();
        Random random = new Random();
        int i = random.nextInt(size);
        return collection.get(i);
    }

    public Object getByField(Class<?> eClass, String fieldName, String fieldValue) {
        return sessionFactory.getCurrentSession().createCriteria(eClass)
                .add(Restrictions.eq(fieldName, fieldValue))
                .uniqueResult();
    }

    @Transactional
    public void saveTest(){
        NewUser user = new NewUser();
        user.setId(4);

        NewProfile newProfile = new NewProfile();
        newProfile.setName("nnnn");
        newProfile.setUser(user);
//        newProfile.setUserId(4);

        sessionFactory.getCurrentSession().save(newProfile);
    }

    @Transactional
    public void fillNewUser() {
        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < 1000000; i++) {
            NewUser newUser = new NewUser();
            newUser.setPassword("p-"+System.currentTimeMillis());
            session.save(newUser);
        }
    }

    @Transactional(readOnly = true)
    public NewUser getNewUserReadOnly(){
        return getNewUser();
    }

    @Transactional
    public NewUser getNewUserNotReadOnly(){
        return getNewUser();
    }


    @Transactional
    public void t(){
        Role role = new Role();
        role.setName("nnnn-"+System.currentTimeMillis());
        role.getPermissions().add(new Permission("p-"+System.currentTimeMillis()));
        role.getPermissions().add(new Permission("p-"+System.currentTimeMillis()));
        role.getPermissions().add(new Permission("p-"+System.currentTimeMillis()));
        sessionFactory.getCurrentSession().save(role);
    }

    @Transactional
    public void t2(){
        Role role = new Role();
        role.setName("nnnn-"+System.currentTimeMillis());
        Permission permission = (Permission) sessionFactory.getCurrentSession().get(Permission.class, 1L);
        role.getPermissions().add(permission);
        sessionFactory.getCurrentSession().save(role);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public  void tGet(){
        List<Role> roleList = sessionFactory.getCurrentSession().createCriteria(Role.class).list();
        System.out.println("as");
    }

    @Transactional
    public  void tSave(){
//        Role role = new Role();
//        role.setName("n-"+System.currentTimeMillis());
//        sessionFactory.getCurrentSession().save(role);
//        sessionFactory.getCurrentSession().flush();

        List<Role> roleList = sessionFactory.getCurrentSession().createCriteria(Role.class).list();
        roleList.get(0).setName("nnnn-"+System.currentTimeMillis());

    }

    private NewUser getNewUser(){
        return (NewUser) sessionFactory.getCurrentSession().createQuery("from NewUser where password = 'p-1411580902721'").uniqueResult();
    }

    private void save(Object o) {
        sessionFactory.getCurrentSession().save(o);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
