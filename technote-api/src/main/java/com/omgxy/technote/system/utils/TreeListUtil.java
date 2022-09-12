package com.omgxy.technote.system.utils;

import com.omgxy.technote.system.entity.dto.TreeNode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TreeListUtil<T> {

    public List<T> convertListToTree(List<T> list) {
        List<T> tree = new ArrayList<>();
            for (T o : list) {
                try {
                    Field field = o.getClass().getDeclaredField("parentId");
                    field.setAccessible(true);
                    if ("0".equals(field.get(o))) {
                        setChildren(list, o);

                        Field pathField = o.getClass().getDeclaredField("path");
                        pathField.setAccessible(true);

                        Field idField = o.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Object oId = idField.get(o);

                        Field labelField = o.getClass().getDeclaredField("label");
                        labelField.setAccessible(true);
                        Object oLabel = labelField.get(o);

                        pathField.set(o, Collections.singletonList(new TreeNode((String) oId, (String) oLabel)));

                        tree.add(o);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    log.error(e.getMessage());
                }
            }
        return tree;
    }

    public void setChildren(List<T> list, T parent) {
        Class<?> bClass = parent.getClass();
        try {
            for (T o : list) {
                Class<?> aClass = o.getClass();
                Field f1 = aClass.getDeclaredField("id");
                f1.setAccessible(true);
                String id = (String) f1.get(parent);

                Field f2 = aClass.getDeclaredField("parentId");
                f2.setAccessible(true);
                String parentId = (String) f2.get(o);
                if (id.equals(parentId)) {
                    List<T> children = (List<T>) bClass.getDeclaredField("children").get(parent);
                    if (children == null) {
                        children = new ArrayList<>();
                        bClass.getDeclaredField("children").set(parent, children);
                    }

                    Field pathField = o.getClass().getDeclaredField("path");
                    pathField.setAccessible(true);

                    Field idField = o.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object oId = idField.get(o);

                    Field labelField = o.getClass().getDeclaredField("label");
                    labelField.setAccessible(true);
                    Object oLabel = labelField.get(o);

                    List<Object> oPath = (List<Object>) pathField.get(o);
                    if (oPath == null) {
                        oPath = Arrays.asList(new TreeNode((String) idField.get(parent), (String) labelField.get(parent)),
                                new TreeNode((String) oId, (String) oLabel));
                    } else {
                        oPath = Collections.singletonList(new TreeNode((String) oId, (String) oLabel));
                    }

                    pathField.set(o, oPath);
                    children.add(o);
                }
            }
            Field f3 = bClass.getDeclaredField("children");
            f3.setAccessible(true);
            List<T> children = (List<T>) f3.get(parent);
            if (children == null || children.isEmpty()) {
                return;
            }
            for (T o : children) {
                setChildren(list, o);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(e.getMessage());
        }

    }
}
