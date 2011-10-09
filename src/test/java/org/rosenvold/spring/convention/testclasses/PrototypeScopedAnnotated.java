package org.rosenvold.spring.convention.testclasses;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Kristian Rosenvold
 */
@Service
@Scope("prototype")
public class PrototypeScopedAnnotated
{
}
